package org.powbot.stream.ops

import WrappedStream
import org.powbot.stream.SimpleStream
import org.powerbot.script.Actionable
import java.util.regex.Pattern

interface ActionableOps<T: Actionable, S: SimpleStream<T, S>>: WrappedStream<T, S> {

    fun action(vararg actions: Pattern?): S {
        return filter(Actionable.Matcher(*actions))
    }
}
