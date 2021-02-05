package org.powbot.stream.widget

import org.powbot.stream.SimpleStream
import org.powbot.stream.ops.IdentifiableOps
import org.powerbot.script.rt4.ClientContext
import org.powerbot.script.rt4.World
import java.util.stream.Stream

open class WorldStream(ctx: ClientContext, stream: Stream<World>) :
    SimpleStream<World, WorldStream>(ctx, stream),
    IdentifiableOps<World, WorldStream>
