package org.powbot.stream.locatable.interactive

import org.powbot.stream.ops.ActionableOps
import org.powerbot.script.rt4.ClientContext
import org.powerbot.script.rt4.Npc
import java.util.stream.*

class NpcStream(ctx: ClientContext, stream: Stream<Npc>) : ActorStream<Npc, NpcStream>(ctx, stream),
    ActionableOps<Npc, NpcStream> {
    override fun nil(): Npc {
        return Npc.NIL
    }
}
