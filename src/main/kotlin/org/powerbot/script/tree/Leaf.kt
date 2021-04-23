package org.powerbot.script.tree

abstract class Leaf<S : TreeScript>(override val script: S, override val name: String) : TreeComponent<S>()

class SimpleLeaf<S : TreeScript>(script: S, name: String, val action: () -> Unit) :
    Leaf<S>(script, name) {
    override fun execute() {
        action()
    }
}
