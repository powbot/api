package org.powbot.stream.ops

import WrappedStream
import org.powbot.stream.SimpleStream
import org.powerbot.script.Identifiable

interface IdentifiableOps<T: Identifiable, S: SimpleStream<T, S>>: WrappedStream<T, S> {

    fun id(vararg ids: Int): S {
        return filter(Identifiable.Matcher(*ids))
    }
}
