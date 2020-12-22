package org.powerbot.bot.inventory

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import org.powerbot.bot.rt4.client.ItemConfig
import org.powerbot.script.*
import org.powerbot.script.rt4.CacheItemConfig
import org.powerbot.script.rt4.ClientContext
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicBoolean
import java.util.logging.Level
import java.util.logging.Logger

@InternalCoroutinesApi
class InventoryWatcher : GameActionListener, MessageListener {

    val logger = Logger.getLogger(javaClass.name)
    var initialized: AtomicBoolean = AtomicBoolean(false)
    var cache = mapOf<Int, Int>()

    private val channel = BroadcastChannel<Boolean>(1)

    init {
        GlobalScope.launch {
            observe()
        }
    }

    suspend fun observe() {
        val check = {
            try {
                if (!ClientContext.ctx().game.loggedIn() || ClientContext.ctx().client().clientState != 30) {
                    initialized.set(false)
                } else {
                    Condition.wait({
                        ClientContext.ctx().players.local().animation() == -1 &&
                            !ClientContext.ctx().players.local().inMotion()
                    }, 500, 20)

                    val items = ClientContext.ctx().inventory.items()
                        .filterNotNull().filter { it.id() > 0 }
                        .groupBy { it.id() }
                        .map { it.key to it.value.map { item -> item.stackSize() }.sum() }.toMap()
                    if (!initialized.get()) {
                        cache = items
                        initialized.set(true)
                    }

                    diffMaps(items, cache)
                    cache = items
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
                if (initialized.get())
                    ClientContext.ctx().bot().dispatcher.dispatch(
                        InventoryChangeEvent(it.key, it.value, it.value, CacheItemConfig.load(ClientContext.ctx().bot().cacheWorker, it.key)?.name)
                    )
            } else if (old[it.key] != it.value) {
                if (initialized.get())
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
                if (initialized.get())
                    ClientContext.ctx().bot().dispatcher.dispatch(
                        InventoryChangeEvent(it.key, it.value * -1, 0, CacheItemConfig.load(ClientContext.ctx().bot().cacheWorker, it.key)?.name)
                    )
            }
        }
    }

    override fun onAction(evt: GameActionEvent) {
        GlobalScope.launch {
            channel.send(true)
        }
    }

    override fun messaged(event: MessageEvent?) {
        if (event?.type() == 108 && event.text() == "Welcome to Old School RuneScape.") {
            initialized.set(false)
        }

        if (event?.type() != 2) {
            GlobalScope.launch {
                channel.send(true)
            }
        }
    }
}
