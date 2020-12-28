package org.powerbot.bot

import org.powerbot.script.*
import java.io.File
import java.util.logging.Logger

class GameActionLogger<C : ClientContext<*>?>(ctx: C) : ClientAccessor<C>(ctx), GameActionListener {

    companion object {
        private val log = Logger.getLogger("Action Events")
    }

    override fun onAction(evt: GameActionEvent) {
        log.info("$evt")
    }
}
