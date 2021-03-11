package org.powbot.stream.locatable.interactive

import org.powbot.stream.ops.ActionableOps
import org.powerbot.bot.rt4.client.internal.INpc
import org.powerbot.script.rt4.ClientContext
import org.powerbot.script.rt4.Npc
import java.util.stream.*

class NpcStream(ctx: ClientContext, stream: Stream<INpc>) : ActorStream<Npc, INpc, NpcStream>(ctx, stream, { Npc(ctx, it) }),
    ActionableOps<Npc, INpc, NpcStream> {
    override fun nil(): Npc {
        return Npc.NIL
    }

}
