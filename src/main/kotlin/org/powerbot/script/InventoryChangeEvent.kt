package org.powerbot.script

import org.powerbot.bot.AbstractEvent
import org.powerbot.bot.EventType
import java.util.*

data class InventoryChangeEvent(val itemId: Int, val quantityChange: Int, val newQuantity: Int) : AbstractEvent(EVENT_ID) {

    companion object {
        val EVENT_ID = EventType.INVENTORY_CHANGE_EVENT.id()
        private const val serialVersionUID = 2305739551668845607L
    }

    /**
     * {@inheritDoc}
     */
    override fun call(e: EventListener) {
        try {
            (e as InventoryChangeListener).onChange(this)
        } catch (ignored: Exception) {
        }
    }
}
