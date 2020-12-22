package org.powbot.walking.obstacle

import org.powerbot.script.ClientContext
import org.powerbot.script.Condition
import org.powerbot.script.Random

object ObstacleHandlerUtils  {

    fun isChatting(): Boolean {
        for (w in ObstacleHandler.ChatWidgets) {
            if (Condition.wait({
                    for (w2 in ObstacleHandler.ChatWidgets) {
                        if (ClientContext.ctx().widgets.widget(w).valid() || ClientContext.ctx().widgets.widget(w2).valid()) {
                            return@wait true
                        }
                    }
                    false
                }, 25, Random.nextInt(4, 6))) {
                return true
            }
        }
        return false
    }
}
