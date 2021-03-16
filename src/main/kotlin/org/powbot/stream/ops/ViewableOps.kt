package org.powbot.stream.ops

import org.powbot.stream.WrappedStream
import org.powbot.stream.SimpleStream
import org.powerbot.script.Viewable

interface ViewableOps<T, I: Viewable, S: SimpleStream<T, I, S>>: WrappedStream<T, I, S> {

    fun viewable(): S {
        return filter { it.inViewport() }
    }

    fun visible(): S {
        return viewable()
    }
}
