package org.powbot.stream.locatable.interactive

import org.powbot.stream.SimpleStream
import org.powbot.stream.ops.LocatableOps
import org.powbot.stream.ops.NameableOps
import org.powerbot.bot.rt4.client.internal.IActor
import org.powerbot.script.rt4.Actor
import org.powerbot.script.rt4.ClientContext
import java.util.stream.Stream

abstract class ActorStream<T : Actor, I: IActor, S : SimpleStream<T, I, S>>(ctx: ClientContext, stream: Stream<I>, wrap: (I) -> T) :
    InteractiveStream<T, I, S>(ctx, stream, wrap),
    LocatableOps<T, I, S>,
    NameableOps<T, I, S>
