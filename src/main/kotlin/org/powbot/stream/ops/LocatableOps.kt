package org.powbot.stream.ops

import org.powbot.stream.WrappedStream
import org.powbot.stream.SimpleStream
import org.powerbot.script.Area
import org.powerbot.script.Locatable

interface LocatableOps<T, I: Locatable, S: SimpleStream<T, I, S>>: WrappedStream<T, I, S> {

    fun at(l: Locatable): S {
        return filter(Locatable.Matcher(l.tile()))
    }

    fun within(radius: Double): S {
        return within(ctx.players.local().tile(), radius)
    }

    fun within(locatable: Locatable, radius: Double): S {
        return filter(Locatable.WithinRange(locatable.tile(), radius))
    }

    fun within(area: Area): S {
        return filter(Locatable.WithinArea(area))
    }

    fun nearest(locatable: Locatable): S {
        return sorted(Locatable.NearestTo(locatable.tile()))
    }

    fun nearest(): S {
        return nearest(ctx.players.local().tile())
    }
}
