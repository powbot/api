package org.powerbot.script

import org.powerbot.bot.AbstractEvent
import org.powerbot.bot.EventType
import java.util.*

data class UpdateNpcsEvent(val timestamp: Long = System.currentTimeMillis()) : AbstractEvent(EVENT_ID) {

    companion object {
        val EVENT_ID = EventType.UPDATE_NPCS_EVENT.id()
        private const val serialVersionUID = 8205739551668844907L
    }

    /**
     * {@inheritDoc}
     */
    override fun call(e: EventListener?) {
        try {
            (e as UpdateNpcsListener).onUpdate(this)
        } catch (ignored: Exception) {
        }
    }
}
