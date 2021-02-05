package org.powbot.stream.ops

import org.powbot.stream.WrappedStream
import org.powbot.stream.SimpleStream
import org.powerbot.script.Filter
import org.powerbot.script.Textable
import java.util.regex.Pattern

interface TextOps<T: Textable, S: SimpleStream<T, S>>: WrappedStream<T, S> {

    fun text(vararg texts: String?): S {
        return filter(Textable.Matcher(*texts))
    }

    fun text(pattern: Pattern): S {
        return filter(Filter { t: T ->
            t.text() != null && pattern.matcher(t.text().trim()).find()
        })
    }

    fun textContains(vararg texts: String?): S {
        val textsLower = texts.filterNotNull().map { it.toLowerCase() }
        return filter(Filter { t: T ->
            val text1 = t.text().toLowerCase().trim()
            textsLower.any { s -> text1.contains(s) }
        })
    }
}
