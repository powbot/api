package org.powerbot.script

import org.powerbot.bot.AbstractEvent
import org.powerbot.bot.EventType
import java.util.*

data class ScriptStateChangedEvent(val oldState: ScriptState, val newState: ScriptState): AbstractEvent(EVENT_ID) {
    companion object {
        val EVENT_ID = EventType.SCRIPT_STATE_CHANGED_EVENT.id()
        private const val serialVersionUID = 2345739551668844907L
    }

    override fun call(e: EventListener?) {
        try {
            (e as ScriptStateListener).onChange(this)
        } catch (ignored: Exception) {
        }
    }
}
