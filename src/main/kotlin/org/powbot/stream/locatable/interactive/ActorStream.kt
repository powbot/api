package org.powbot.stream.locatable.interactive

import org.powbot.stream.SimpleStream
import org.powbot.stream.ops.LocatableOps
import org.powbot.stream.ops.NameableOps
import org.powerbot.script.rt4.Actor
import org.powerbot.script.rt4.ClientContext
import java.util.stream.Stream

open class ActorStream<T : Actor, S : SimpleStream<T, S>>(ctx: ClientContext, stream: Stream<T>) :
    InteractiveStream<T, S>(ctx, stream),
    LocatableOps<T, S>,
    NameableOps<T, S>
