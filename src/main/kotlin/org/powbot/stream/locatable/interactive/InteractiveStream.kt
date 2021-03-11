package org.powbot.stream.locatable.interactive

import org.powbot.stream.SimpleStream
import org.powbot.stream.ops.ViewableOps
import org.powerbot.script.Interactive
import org.powerbot.script.rt4.ClientContext
import java.util.stream.Stream

abstract class InteractiveStream<T, I: Interactive, S: SimpleStream<T, I, S>>(ctx: ClientContext, stream: Stream<I>, wrap: (I) -> T) : SimpleStream<T, I, S>(ctx, stream, wrap),
    ViewableOps<T, I, S>
