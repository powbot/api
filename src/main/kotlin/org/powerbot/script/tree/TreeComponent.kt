package org.powerbot.script.tree

import org.powerbot.script.rt4.ClientContext

abstract class TreeComponent<S : TreeScript> {

    abstract val script: S

    val ctx: ClientContext get() = script.ctx

    /**
     * Leave this be, used in the TreeScript to update the logic
     */
    abstract fun execute()

    /**
     * Name of the TreeComponent for debugging purposes
     */
    abstract val name: String
}
