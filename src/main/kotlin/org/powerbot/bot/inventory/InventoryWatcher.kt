package org.powerbot.bot.inventory

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import org.powerbot.script.*
import org.powerbot.script.rt4.CacheItemConfig
import org.powerbot.script.rt4.ClientContext
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicBoolean
import java.util.logging.Level
import java.util.logging.Logger

@FlowPreview
@InternalCoroutinesApi
class InventoryWatcher : GameActionListener, MessageListener, GameTickListener {

    val logger = Logger.getLogger(javaClass.name)
    var initialized: Boolean = false
    var loginTime = 0L
    var cache = mapOf<Int, Int>()

    @ExperimentalCoroutinesApi
    private val channel = BroadcastChannel<Boolean>(1)

    init {
        GlobalScope.launch {
            observe()
        }
    }

    @FlowPreview
    suspend fun observe() {
        val check = {
            try {
                if (!ClientContext.ctx().game.loggedIn() || ClientContext.ctx().client().clientState != 30) {
                    initialized = false
                } else {
                    val items = ClientContext.ctx().inventory.items()
                        .filterNotNull().filter { it.id() > 0 }
                        .groupBy { it.id() }
                        .map { it.key to it.value.map { item -> item.stackSize() }.sum() }.toMap()
                    if (items.size > 0 || (loginTime > 0 && System.currentTimeMillis() - loginTime > 1500)) {
                        if (!initialized) {
                            cache = items
                            initialized = true
                        } else {
                            diffMaps(items, cache)
                            cache = items
                        }
                    }
                }
            } catch (t: Throwable) {
                logger.log(Level.WARNING, "Failed watching inventory", t)
            }
        }

        channel
            .asFlow()
            .debounce(100)
            .collect {
                check()
            }
    }

    private fun diffMaps(new: Map<Int, Int>, old: Map<Int, Int>) {
        new.forEach {
            if (!old.containsKey(it.key)) {
                if (initialized)
                    ClientContext.ctx().bot().dispatcher.dispatch(
                        InventoryChangeEvent(it.key, it.value, it.value, CacheItemConfig.load(ClientContext.ctx().bot().cacheWorker, it.key)?.name)
                    )
            } else if (old[it.key] != it.value) {
                if (initialized)
                    ClientContext.ctx().bot().dispatcher.dispatch(InventoryChangeEvent(
                        it.key,
                        it.value - (old[it.key] ?: 0),
                        it.value,
                        CacheItemConfig.load(ClientContext.ctx().bot().cacheWorker, it.key)?.name
                    ))
            }
        }
        old.forEach {
            if (!new.containsKey(it.key)) {
                if (initialized)
                    ClientContext.ctx().bot().dispatcher.dispatch(
                        InventoryChangeEvent(it.key, it.value * -1, 0, CacheItemConfig.load(ClientContext.ctx().bot().cacheWorker, it.key)?.name)
                    )
            }
        }
    }

    @ExperimentalCoroutinesApi
    override fun onAction(evt: GameActionEvent) {
        GlobalScope.launch {
            channel.send(true)
            val start = System.currentTimeMillis()
            while (System.currentTimeMillis() - start < 5000 &&
                (ClientContext.ctx().players.local().animation() == -1 &&
                !ClientContext.ctx().players.local().inMotion())
            ) {
                delay(100)
            }
            channel.send(true)
        }
    }

    @ExperimentalCoroutinesApi
    override fun messaged(event: MessageEvent?) {
        if (event?.type() == 108 && event.text() == "Welcome to Old School RuneScape.") {
            loginTime = System.currentTimeMillis()
            initialized = false
        }

        if (event?.type() != 2) {
            GlobalScope.launch {
                channel.send(true)
            }
        }
    }

    @ExperimentalCoroutinesApi
    override fun onGameTick(evt: GameTickEvent) {
        GlobalScope.launch {
            channel.send(true)
        }
    }
}
