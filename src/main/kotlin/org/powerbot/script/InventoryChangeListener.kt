package org.powerbot.script

import java.util.*

interface InventoryChangeListener: EventListener {

    /**
     * Call upon your inventory changing
     * @param evt - A wrapper containing information about the event which occured
     */
    fun onChange(evt: InventoryChangeEvent)
}
