package org.powerbot.script

import org.powerbot.bot.AbstractEvent
import org.powerbot.bot.EventType
import java.util.*

data class GameTickEvent(val timestamp: Long = System.currentTimeMillis()) : AbstractEvent(EVENT_ID) {

    companion object {
        val EVENT_ID = EventType.GAME_TICK_EVENT.id()
        private const val serialVersionUID = 5205739551668844907L
    }

    /**
     * {@inheritDoc}
     */
    override fun call(e: EventListener?) {
        try {
            (e as GameTickListener).onGameTick(this)
        } catch (ignored: Exception) {
        }
    }
}
