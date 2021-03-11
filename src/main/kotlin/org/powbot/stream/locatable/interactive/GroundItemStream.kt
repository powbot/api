package org.powbot.stream.locatable.interactive

import org.powbot.stream.ops.ActionableOps
import org.powbot.stream.ops.LocatableOps
import org.powbot.stream.ops.NameableOps
import org.powerbot.bot.rt4.client.internal.IItemNode
import org.powerbot.script.rt4.ClientContext
import org.powerbot.script.rt4.GroundItem
import java.util.stream.*

//TODO improve this
class GroundItemStream(ctx: ClientContext, stream: Stream<GroundItem>)
    : InteractiveStream<GroundItem, GroundItem, GroundItemStream>(ctx, stream, {  it }),
    ActionableOps<GroundItem, GroundItem, GroundItemStream>,
    LocatableOps<GroundItem, GroundItem, GroundItemStream>,
    NameableOps<GroundItem, GroundItem, GroundItemStream> {

    override fun nil(): GroundItem {
        return GroundItem.NIL
    }
}
