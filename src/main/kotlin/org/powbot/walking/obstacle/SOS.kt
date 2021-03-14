package org.powbot.walking.obstacle

import org.powbot.walking.obstacle.ObstacleHandlerUtils.isChatting
import org.powerbot.bot.rt4.client.internal.IWidget
import org.powerbot.script.ClientContext
import org.powerbot.script.Condition
import org.powerbot.script.Random
import org.powerbot.script.Tile
import org.powerbot.script.rt4.Component
import org.powerbot.script.rt4.Game
import org.powerbot.script.rt4.Player

object SOS {

    fun handleSOSWarning(): Boolean {
        val ctx = ClientContext.ctx()
        val warning: IWidget = ctx.widgets.widget(579).component(17)
        return if (warning.valid()) {
            ctx.controller.script().log.info("SOS warning visible. handling it")
            warning.click()
        } else {
            false
        }
    }

    fun handleSOSDoors(): Boolean {
        val ctx = ClientContext.ctx()
        if (inSOS()) {
            val oldTile: Tile = ctx.players.local().tile()
            if (ctx.game.crosshair() === Game.Crosshair.ACTION || isChatting()) {
                if (Condition.wait({ isChatting() || oldTile != ctx.players.local().tile() }, 15, 100)) {
                    return if (isChatting()) {
                        while (isChatting()) {
                            if (ctx.chat.canContinue()) {
                                ctx.input.send("{VK_SPACE}")
                            } else {
                                var supportQuestion = false
                                var chatOptions = 0
                                for (chatOption in ctx.chat.select()) {
                                    chatOptions++
                                    ctx.controller.script().log.info(chatOption.text())
                                    when (chatOption.text()) {
                                        "Authenticator and two-step login on my registered mail.", "Nobody.", "No.", "Report the player for phishing.", "Don't give them my password.", "Nothing, it's a fake.", "Don't give out your password to anyone. Not even close friends.", "Set up 2 step authentication with my email provider.", "Don't give them the information and send an 'Abuse report'.", "No, you should never allow anyone to level your account.", "Only on the Old School RuneScape website.", "Read the text and follow the advice given.", "Decline the offer and report that player.", "No way! You'll just take my gold for your own! Reported!", "Secure my device and reset my password.", "Use the Account Recovery System.", "Don't share your information and report the player.", "No way! I'm reporting you to Jagex!", "No, you should never buy an account.", "Don't type in my password backwards and report the player.", "Report the stream as a scam. Real Jagex streams have a 'verified' mark.", "Virus scan my device then change my password.", "Delete it - it's a fake!", "Do not visit the website and report the player who messaged you.", "Do not visit the site and report the player who messaged you.", "Me.", "Don't tell them anything and click the 'Report Abuse' button.", "Politely tell them no and then use the 'Report Abuse' button.", "Report the incident and do not click any links." -> {
                                            supportQuestion = true
                                            ctx.input.send((chatOption.index + 1).toString())
                                            Condition.sleep(Random.nextInt(400, 1200))
                                        }
                                    }
                                }
                                if (!supportQuestion) {
                                    ctx.input.send(java.lang.String.valueOf(Random.nextInt(1, chatOptions)))
                                }
                            }
                        }
                        Condition.wait({ !isChatting() }, 100, 12)
                    } else {
                        true
                    }
                }
            }
        }
        return false
    }

    fun inSOS(): Boolean {
        val p: Player = ClientContext.ctx().players.local()
        return (ObstacleHandler.VaultOfWarArea.contains(p.tile())
            || ObstacleHandler.PitOfPestilenceArea.contains(p.tile())
            || ObstacleHandler.CatacombsOfFamineArea.contains(p.tile())
            || ObstacleHandler.SephulchreOfDeathArea.contains(p.tile()))
    }
}
