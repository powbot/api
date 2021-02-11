package org.powbot.stream.item

import org.powbot.stream.locatable.interactive.InteractiveStream
import org.powbot.stream.SimpleStream
import org.powbot.stream.ops.ActionableOps
import org.powbot.stream.ops.IdentifiableOps
import org.powbot.stream.ops.NameableOps
import org.powerbot.script.rt4.ClientContext
import org.powerbot.script.rt4.Item
import java.util.stream.*

abstract class ItemStream<T : Item, S : SimpleStream<T, S>>(ctx: ClientContext, stream: Stream<T>) :
    InteractiveStream<T, S>(ctx, stream),
    ActionableOps<T, S>,
    IdentifiableOps<T, S>,
    NameableOps<T, S> {

    /**
     * Count how many items are in the stream
     *
     * @param countStacks - true to sum up stack sizes, false to just get a count of the entities
     * @return count
     */
    fun count(countStacks: Boolean): Long {
        if (!countStacks) {
            return count()
        }

        return filter { it != null }.mapToLong { it.stackSize().toLong() }.sum()
    }
}
