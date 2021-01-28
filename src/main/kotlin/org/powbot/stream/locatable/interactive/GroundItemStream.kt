package org.powbot.stream.locatable.interactive

import org.powbot.stream.ops.ActionableOps
import org.powbot.stream.ops.LocatableOps
import org.powbot.stream.ops.NameableOps
import org.powerbot.script.rt4.ClientContext
import org.powerbot.script.rt4.GroundItem
import java.util.stream.*

class GroundItemStream(ctx: ClientContext, stream: Stream<GroundItem>) : InteractiveStream<GroundItem, GroundItemStream>(ctx, stream),
    ActionableOps<GroundItem, GroundItemStream>,
    LocatableOps<GroundItem, GroundItemStream>,
    NameableOps<GroundItem, GroundItemStream>
