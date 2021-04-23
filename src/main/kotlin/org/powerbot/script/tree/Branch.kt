package org.powerbot.script.tree

abstract class Branch<S : TreeScript>(override val script: S) : TreeComponent<S>() {

    abstract val successComponent: TreeComponent<S>
    abstract val failedComponent: TreeComponent<S>

    abstract fun validate(): Boolean

    /**
     * Executes either successComponent or failedComponent depending on whether validate() return true or false
     */
    override fun execute() {
        if (validate()) {
            if (script.debugComponents) {
                println("$name was successful, executing: ${successComponent.name}")
            }
            successComponent.execute()
        } else {
            if (script.debugComponents) {
                println("$name was unsuccessful, executing: ${failedComponent.name}")
            }
            failedComponent.execute()
        }
    }

}
