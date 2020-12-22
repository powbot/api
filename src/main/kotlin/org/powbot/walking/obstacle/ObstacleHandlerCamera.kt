package org.powbot.walking.obstacle

import org.powerbot.script.ClientContext
import org.powerbot.script.Input
import org.powerbot.script.Random
import org.powerbot.script.rt4.Camera
import org.powerbot.script.rt4.Widget
import java.awt.Point
import java.awt.event.MouseEvent
import kotlin.math.abs

object ObstacleHandlerCamera  {


    fun turnCameraOrientation(orientation: Int) {
        val useMiddleMouse: Boolean = Random.nextInt(0, 100) > ObstacleHandler.rGaussian
        when (orientation) {
            0 -> changeAngle(140, 200, useMiddleMouse)
            1 -> changeAngle(100, 160, useMiddleMouse)
            2 -> changeAngle(60, 120, useMiddleMouse)
            3 -> changeAngle(20, 90, useMiddleMouse)
            4 -> changeAngle(330, 360, useMiddleMouse)
            5 -> changeAngle(300, 360, useMiddleMouse)
            6 -> changeAngle(230, 300, useMiddleMouse)
            7 -> changeAngle(190, 250, useMiddleMouse)
        }
    }
    fun changeAngle(min: Int, max: Int, useMiddleMouse: Boolean) {
        val ctx = ClientContext.ctx()

        if (ctx.camera.yaw() < min || ctx.camera.yaw() > max) {
            if (useMiddleMouse) {
                turnTo(Random.nextInt(min, max), Random.nextInt(50, 99))
            } else {
                ctx.camera.pitch(Random.nextInt(50, 99))
                ctx.camera.angle(Random.nextInt(min, max))
            }
        }
    }

    fun turnTo(yaw: Int, pitch: Int) {
        val ctx = ClientContext.ctx()
        val camera: Camera = ctx.camera
        val input: Input = ctx.input
        input.speed(65)
        val vector: Point = getCameraVector(
            camera.yaw(),
            camera.pitch(),
            yaw,
            pitch
        )
            ?: return // Retrieve the screen vector for the camera turn.

        // Define the min and max screen boundaries in which the vector can be dragged.
        var minX: Int
        var minY: Int
        var maxX: Int
        var maxY: Int
        minY = 0
        minX = minY // By default, we assume that the base of the boundary starts at 0.
        val game: Widget = ctx.widgets.widget(161) // This is the game widget.
        val screen = game.component(12) // This is the screen widget.
        maxX = screen.width() // By default, we assume that the max of the boundary end with the screen.
        maxY = screen.height()

        // Adjust the boundaries based on the values of the components in the screen vector.
        if (vector.x < 0) {
            minX = -vector.x
        } else {
            maxX -= vector.x
        }
        if (vector.y < 0) {
            minY = -vector.y
        } else {
            maxY -= vector.y
        }

        // Defines the range of X and Y.
        val xRange = maxX - minX
        val yRange = maxY - minY

        // Finds a random starting point to start the drag.
        val randX: Int = minX + if (xRange > 0) Random.nextInt(0, xRange) else 0
        val randY: Int = minY + if (yRange > 0) Random.nextInt(0, yRange) else 0

        // Drags the camera from the starting point by the screen vector amount.
        input.move(Point(randX, randY))
        input.drag(Point(randX + vector.x, randY + vector.y), MouseEvent.BUTTON2)
    }

    private fun getCameraVector(startYaw: Int, startPitch: Int, endYaw: Int, endPitch: Int): Point? {
        val cPitch = endPitch - startPitch.toDouble() // Defines the change in pitch required.

        // Yaw is a periodic feature [0, 359]. The change in pitch may differ across the end-point of the cycle.
        val cYawCyclA = endYaw + 360 - startYaw.toDouble() // Forwards across the 360 endpoint.
        val cYawCyclB = endYaw - (startYaw + 360).toDouble() // Backwards across the 360 endpoint.
        val cYawNorm = endYaw - startYaw.toDouble() // Change in-between [0, 359], no endpoint crossed.
        val cYaw: Double // Defines the shortest change in yaw required.

        // Compare the absolute differences to determine the shortest path.
        cYaw = if (abs(cYawNorm) < abs(cYawCyclA) && abs(cYawNorm) < abs(cYawCyclB)) {
            cYawNorm
        } else if (abs(cYawCyclA) < abs(cYawNorm) && abs(cYawCyclA) < abs(cYawCyclB)) {
            cYawCyclA
        } else {
            cYawCyclB
        }

        // Define the screen vector components. Math.PI and (4/3) represent calculated slopes.
        val changeX = (-Math.PI * cYaw).toInt()
        val changeY = (4.0 / 3 * cPitch).toInt()
        return Point(changeX, changeY)
    }
}
