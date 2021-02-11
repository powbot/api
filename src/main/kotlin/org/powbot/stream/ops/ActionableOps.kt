package org.powbot.stream.ops

import org.powbot.stream.WrappedStream
import org.powbot.stream.SimpleStream
import org.powerbot.script.Actionable
import java.util.regex.Pattern

interface ActionableOps<T: Actionable, S: SimpleStream<T, S>>: WrappedStream<T, S> {

    fun action(actions: Collection<String>): S {
        return action(*actions.toTypedArray())
    }

    fun action(vararg actions: String?): S {
        val a: List<Pattern> = actions.map {
            Pattern.compile(Pattern.quote(it), Pattern.CASE_INSENSITIVE)
        }
        return action(*a.toTypedArray())
    }

    fun action(vararg actions: Pattern?): S {
        return filter(Actionable.Matcher(*actions))
    }
}
