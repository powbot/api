package org.powerbot.script

import org.powbot.walking.WebWalkingResult

interface WebWalkingService {
    fun walkTo(loc: Locatable, refreshQuests: Boolean = false): Boolean

    fun moveTo(loc: Locatable, refreshQuests: Boolean = false, forceWeb: Boolean = false): WebWalkingResult
}
