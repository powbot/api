package org.powbot.stream.locatable.interactive

import org.powbot.stream.SimpleStream
import org.powbot.stream.ops.ViewableOps
import org.powerbot.script.Interactive
import org.powerbot.script.rt4.ClientContext
import java.util.stream.Stream

abstract class InteractiveStream<T: Interactive, S: SimpleStream<T, S>>(ctx: ClientContext, stream: Stream<T>) : SimpleStream<T, S>(ctx, stream),
    ViewableOps<T, S>
