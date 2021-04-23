package org.powerbot.script.tree

abstract class Leaf<S : TreeScript>(override val script: S) : TreeComponent<S>()

class SimpleLeaf<S : TreeScript>(override val script: S, override val name: String, val action: () -> Unit) :
    TreeComponent<S>() {
    override fun execute() {
        action()
    }
}
