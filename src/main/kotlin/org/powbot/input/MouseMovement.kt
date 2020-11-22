package org.powbot.input

import java.awt.Point
import java.util.concurrent.Callable


interface MouseMovementCompleted {
    fun onCompleted(success: Boolean)
}

data class MouseMovement(
    val target: Callable<Point>,
    val valid: Callable<Boolean>,
    val onCompleted: MouseMovementCompleted,
    var canceled: Boolean = false
) {
    fun canRun(): Boolean {
        return !canceled && valid.call()
    }
}
