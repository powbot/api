package org.powerbot.script

import java.util.*

interface GameActionListener: EventListener {

    /**
     * Call upon actions happening in game
     * @param evt - A wrapper containing information about the event which occured
     */
    fun onAction(evt: GameActionEvent)
}
