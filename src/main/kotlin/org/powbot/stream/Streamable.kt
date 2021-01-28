package org.powbot.stream

interface Streamable<T> {

    fun toStream(): T
}
