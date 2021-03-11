package org.powbot.stream.ops

import org.powbot.stream.WrappedStream
import org.powbot.stream.SimpleStream
import org.powerbot.script.Nameable
import java.util.regex.Pattern

interface NameableOps<T, I: Nameable, S: SimpleStream<T, I, S>>: WrappedStream<T, I, S> {

    fun name(vararg names: String): S {
        val patterns = names.map { Pattern.compile(Pattern.quote(it), Pattern.CASE_INSENSITIVE) }
        return name(*patterns.toTypedArray())
    }

    fun name(vararg names: Pattern): S {
        return filter(Nameable.Matcher(*names))
    }
}
