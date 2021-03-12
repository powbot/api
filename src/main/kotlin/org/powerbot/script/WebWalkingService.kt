package org.powerbot.script

import org.powbot.walking.WebWalkingResult
import java.util.concurrent.Callable
import java.util.function.Predicate

interface WebWalkingService {
    fun walkTo(loc: Locatable, refreshQuests: Boolean): Boolean

    fun moveTo(loc: Locatable, refreshQuests: Boolean, forceWeb: Boolean): WebWalkingResult

    fun moveTo(
        loc: Locatable,
        refreshQuests: Boolean,
        forceWeb: Boolean,
        walkUntil: Callable<Boolean>,
        runMin: Int,
        runMax: Int
    ): WebWalkingResult
}
