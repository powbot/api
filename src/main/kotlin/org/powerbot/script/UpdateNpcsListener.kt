package org.powerbot.script

import java.util.*

interface UpdateNpcsListener: EventListener {

    /**
     * Call upon each npc update event
     */
    fun onUpdate(evt: UpdateNpcsEvent)
}
