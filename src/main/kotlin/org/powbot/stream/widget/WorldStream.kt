package org.powbot.stream.widget

import org.powbot.stream.SimpleStream
import org.powbot.stream.ops.IdentifiableOps
import org.powerbot.script.rt4.ClientContext
import org.powerbot.script.rt4.World
import java.util.stream.Stream

open class WorldStream(ctx: ClientContext, stream: Stream<World>) :
    SimpleStream<World, World, WorldStream>(ctx, stream, { it }),
    IdentifiableOps<World, World,WorldStream> {
    override fun nil(): World {
        return World.NIL
    }

    override fun nilWrapped(): World {
        return World.NIL
    }
}
