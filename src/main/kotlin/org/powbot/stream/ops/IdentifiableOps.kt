package org.powbot.stream.ops

import org.powbot.stream.WrappedStream
import org.powbot.stream.SimpleStream
import org.powerbot.script.Identifiable

interface IdentifiableOps<T, I: Identifiable, S: SimpleStream<T, I, S>>: WrappedStream<T, I, S> {

    fun id(vararg ids: Int): S {
        return filter(Identifiable.Matcher(*ids))
    }
}
