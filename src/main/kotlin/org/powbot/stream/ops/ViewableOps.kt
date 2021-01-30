package org.powbot.stream.ops

import WrappedStream
import org.powbot.stream.SimpleStream
import org.powerbot.script.Viewable

interface ViewableOps<T: Viewable, S: SimpleStream<T, S>>: WrappedStream<T, S> {

    fun viewable(): S {
        return filter { it.inViewport() }
    }
}
