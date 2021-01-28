package org.powbot.stream.item

import org.powbot.stream.locatable.interactive.InteractiveStream
import org.powbot.stream.SimpleStream
import org.powbot.stream.ops.ActionableOps
import org.powbot.stream.ops.NameableOps
import org.powerbot.script.rt4.ClientContext
import org.powerbot.script.rt4.Item
import java.util.stream.*

open class ItemStream<T: Item, S: SimpleStream<T, S>>(ctx: ClientContext, stream: Stream<T>) : InteractiveStream<T, S>(ctx, stream),
    ActionableOps<T, S>,
    NameableOps<T, S>
