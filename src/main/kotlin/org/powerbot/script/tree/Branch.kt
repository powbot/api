package org.powerbot.script.tree

abstract class Branch<S : TreeScript>(override val script: S) : TreeComponent<S>() {

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
            println("$name was ${if (success) "successful" else "unsuccessful"}, executing: ${successComponent.name}")
        }
        if (nextComp is Leaf) {
            script.lastLeaf = nextComp
        }
        nextComp.execute()
    }

}
