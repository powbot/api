package org.powbot.stream.locatable.interactive

import org.powerbot.script.rt4.ClientContext
import org.powerbot.script.rt4.Player
import java.util.stream.*

class PlayerStream(ctx: ClientContext, stream: Stream<Player>) : ActorStream<Player, PlayerStream>(ctx, stream) {
    override fun nil(): Player {
        return Player.NIL
    }
}
