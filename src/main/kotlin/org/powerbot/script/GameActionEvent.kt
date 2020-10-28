package org.powerbot.script

import org.powerbot.bot.AbstractEvent
import org.powerbot.bot.EventType
import java.util.*

enum class GameActionOpcode(val opcode: Int) {
    ClickTileViewport(23), NormalClick(1006), WieldItem(34),
    UseItemFirst(38), UseItemSecond(31), ClickWidget(57),
    InteractNpc(9), AttackNpc(10), ClickContinue(30),
    ExamineObject(1002), InteractObject(3), ExamineNpc(1003);

    companion object {
        fun forOpcode(opcode: Int): GameActionOpcode? {
           return  values().find { it.opcode == opcode }
        }
    }
}
data class GameActionEvent(val var0: Int, val widgetId: Int, val rawOpcode: Int, val id: Int,
                      val interaction: String, val rawEntityName: String, val mouseX: Int, val mouseY: Int) : AbstractEvent(EVENT_ID) {

    companion object {
        val EVENT_ID = EventType.DO_ACTION_EVENT.id()
        private const val serialVersionUID = 2205739551668844907L
    }

    fun opcode(): GameActionOpcode? {
        return GameActionOpcode.forOpcode(rawOpcode)
    }

    fun entityName(): String {
        return StringUtils.stripHtml(rawEntityName)
    }

    /**
     * {@inheritDoc}
     */
    override fun call(e: EventListener) {
        try {
            (e as GameActionListener).onAction(this)
        } catch (ignored: Exception) {
        }
    }
}
