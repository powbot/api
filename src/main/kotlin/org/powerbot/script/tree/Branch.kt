package org.powerbot.script.tree

import java.util.logging.Level
import java.util.logging.Logger

abstract class Branch<S : TreeScript>(override val script: S) : TreeComponent<S>() {

    private val logger = Logger.getLogger(javaClass.name)

    abstract val successComponent: TreeComponent<S>
    abstract val failedComponent: TreeComponent<S>

    abstract fun validate(): Boolean

    /**
     * Executes either successComponent or failedComponent depending on whether validate() return true or false
     */
    override fun execute() {
        val success = validate()
        val nextComp = if (success) successComponent else failedComponent
        if (script.debugComponents) {
            logger.log(
                Level.INFO,
                "$name was ${if (success) "successful" else "unsuccessful"}, executing: ${nextComp.name}"
            )
        }
        if (nextComp is Leaf) {
            script.lastLeaf = nextComp
        }
        nextComp.execute()
    }

}
