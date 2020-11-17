package org.powerbot.bot

import org.powerbot.script.*
import java.util.logging.Logger

class InventoryChangeLogger<C : ClientContext<*>?>(ctx: C) : ClientAccessor<C>(ctx), InventoryChangeListener {

    companion object {
        private val log = Logger.getLogger("Inventory Change Events")
    }

    override fun onChange(evt: InventoryChangeEvent) {
        log.info("$evt")
    }
}
