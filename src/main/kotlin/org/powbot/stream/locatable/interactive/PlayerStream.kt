package org.powbot.stream.locatable.interactive

import org.powerbot.bot.rt4.client.internal.IPlayer
import org.powerbot.script.rt4.ClientContext
import org.powerbot.script.rt4.Player
import java.util.stream.*

class PlayerStream(ctx: ClientContext, stream: Stream<IPlayer>)
    : ActorStream<Player, IPlayer, PlayerStream>(ctx, stream, { Player(ctx, it) }) {
    override fun nil(): Player {
        return Player.NIL
    }
}
