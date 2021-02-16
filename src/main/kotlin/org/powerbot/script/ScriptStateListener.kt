package org.powerbot.script

import java.util.*

interface ScriptStateListener: EventListener {

    /**
     * Call upon script state changing
     * @param evt - A wrapper containing information about the event which occured
     */
    fun onChange(evt: ScriptStateChangedEvent)
}
