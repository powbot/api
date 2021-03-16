package org.powbot.stream.locatable.interactive

import org.powbot.stream.ops.ActionableOps
import org.powbot.stream.ops.IdentifiableOps
import org.powerbot.bot.rt4.client.internal.INpc
import org.powerbot.script.rt4.ClientContext
import org.powerbot.script.rt4.Npc
import org.powerbot.script.rt4.Npcs
import java.util.stream.Stream

class NpcStream(ctx: ClientContext, stream: Stream<INpc>) : ActorStream<Npc, INpc, NpcStream>(ctx, stream, { Npc(ctx, it) }),
    IdentifiableOps<Npc, INpc, NpcStream>,
    ActionableOps<Npc, INpc, NpcStream> {
    override fun nil(): INpc {
        return Npcs.NIL
    }

    override fun nilWrapped(): Npc {
        return Npc.NIL
    }

}
