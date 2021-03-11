package org.powbot.stream.locatable

import org.powbot.stream.SimpleStream
import org.powbot.stream.ops.LocatableOps
import org.powerbot.script.Locatable
import org.powerbot.script.rt4.ClientContext
import java.util.stream.Stream

abstract class LocatableStream<T, I: Locatable, S: SimpleStream<T, I, S>>(ctx: ClientContext, stream: Stream<I>, wrap: (I) -> T) :
    SimpleStream<T, I, S>(ctx, stream, wrap),
    LocatableOps<T, I, S>
