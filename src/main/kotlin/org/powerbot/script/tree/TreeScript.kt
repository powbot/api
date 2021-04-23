package org.powerbot.script.tree

import org.powerbot.script.Condition.sleep
import org.powerbot.script.PollingScript
import org.powerbot.script.Random
import org.powerbot.script.rt4.ClientContext

/**
 * TreeScript
 * An implementation of {@link PollingScript} which determines action using a tree-shaped logic flow.
 *
 * @param <C> the type of client
 */
abstract class TreeScript : PollingScript<ClientContext>() {

    /**
     * The root component is the first component executed and can be seen as the base (or stump) of the tree
     */
    abstract val rootComponent: TreeComponent<*>

    abstract val debugComponents: Boolean

    val ctx: ClientContext get() = super.ctx

    var lastLeaf: Leaf<*> = SimpleLeaf(this, "Init") {}

    override fun poll() {
        rootComponent.execute()
        sleep(Random.nextInt(100, 200))
    }

}

/**
 * Example of what using this script logic looks like
 */
class TestScript(override val debugComponents: Boolean = true) : TreeScript() {
    override val rootComponent: TreeComponent<TestScript>
        get() = TestBranch(this)
}

class TestBranch(override val script: TestScript) : Branch<TestScript>(script) {

    override val successComponent: TreeComponent<TestScript> =
        SimpleLeaf(script, "Deposit Inventory") { ctx.bank.depositInventory() }

    override val failedComponent: TreeComponent<TestScript> = TestLeaf(script)

    override val name: String = "TestBranch - Is Bank open"

    override fun validate(): Boolean {
        return ctx.bank.opened()
    }
}

class TestLeaf(override val script: TestScript) : Leaf<TestScript>(script, "Opening Bank") {
    override fun execute() {
        ctx.bank.open()
    }
}

