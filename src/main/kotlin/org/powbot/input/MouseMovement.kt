package org.powbot.input

import java.awt.Point
import java.util.concurrent.Callable

data class MouseMovement(
    val target: Callable<Point>,
    val valid: Callable<Boolean>
) {
    fun canRun(): Boolean {

        return valid.call()
    }
}
