package org.powerbot.script

interface WebWalkingService {
    fun walkTo(loc: Locatable, refreshQuests: Boolean = false): Boolean
}
