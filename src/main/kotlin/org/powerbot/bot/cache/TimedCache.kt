package org.powerbot.bot.cache

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.ConcurrentHashMap

data class TimedCacheEntry<T>(val obj: T?, var lastAccess: Long)

abstract class TimedCache<I, T>() {
    val cache: MutableMap<I, TimedCacheEntry<T>> = ConcurrentHashMap<I, TimedCacheEntry<T>>()

    val Timeout = 120000

    init {
        GlobalScope.launch {
            while (true) {
                cleanup()
                delay(60000)
            }
        }
    }

    abstract fun load(id: I): T?

    fun get(id: I): T? {
        if (cache.containsKey(id)) {
            val entry = cache[id]
            entry!!.lastAccess = System.currentTimeMillis()
            return entry.obj
        }

        val config: T? = load(id)
        add(id, config)

        return config
    }

    fun add(id: I, obj: T?) {
        cache[id] = TimedCacheEntry(obj, System.currentTimeMillis())
    }

    private fun cleanup() {
        cache.entries.filter {
            System.currentTimeMillis() - it.value.lastAccess > Timeout
        }.forEach {
            cache.remove(it.key)
        }
    }
}
