package org.powbot.stream.locatable

import org.powbot.stream.SimpleStream
import org.powbot.stream.ops.LocatableOps
import org.powerbot.script.Locatable
import org.powerbot.script.rt4.ClientContext
import java.util.stream.Stream

abstract class LocatableStream<T: Locatable, S: SimpleStream<T, S>>(ctx: ClientContext, stream: Stream<T>) : SimpleStream<T, S>(ctx, stream),
    LocatableOps<T, S>
