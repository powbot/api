package org.powerbot.bot.model

import java.awt.Point
import java.awt.Polygon
import java.util.*
import java.util.function.Consumer
import java.util.stream.Collectors


/**
 * Credits to http://www.sanfoundry.com/java-program-implement-quick-hull-algorithm-find-convex-hull/
 */
object QuickHuller {
    fun hull(points: MutableList<Point>): Polygon? {
        val hullPoly = Polygon()
        if (points.size < 3) {
            points.forEach(Consumer { p: Point -> hullPoly.addPoint(p.x, p.y) })
            return hullPoly
        }

        val sorted = points.sortedBy { it.x }
        val leftMost: Point = sorted.first()
        val rightMost: Point = sorted.last()
        val hullPoints: MutableList<Point> = mutableListOf(leftMost, rightMost)

        points.remove(leftMost)
        points.remove(rightMost)

        val right: MutableList<Point> = mutableListOf()
        val left: MutableList<Point> = mutableListOf()

        points.forEach {
            if (getPosition(leftMost, rightMost, it) == 1)
                right.add(it)
            else
                left.add(it)
        }

        hull(leftMost, rightMost, right, hullPoints)
        hull(rightMost, leftMost, left, hullPoints)
        hullPoints.forEach(Consumer { p: Point -> hullPoly.addPoint(p.x, p.y) })

        return hullPoly
    }

    private fun hull(leftMost: Point, rightMost: Point, points: MutableList<Point>, hullPoints: MutableList<Point>) {
        if (points.isEmpty()) {
            return
        }

        val rightPos = hullPoints.indexOf(rightMost)
        val furthestPoint: Point = points.maxByOrNull { distance(leftMost, rightMost, it) } ?: return
        hullPoints.add(rightPos, furthestPoint)
        points.remove(furthestPoint)

        val finalFurthestPoint: Point = furthestPoint
        val toLeft = mutableListOf<Point>()
        val toRight = mutableListOf<Point>()
        points.forEach {
            if (getPosition(leftMost, finalFurthestPoint, it) == 1)
                toLeft.add(it)
            else if (getPosition(finalFurthestPoint, rightMost, it) == 1) {
                toRight.add(it)
            }
        }

        hull(leftMost, furthestPoint, toLeft, hullPoints)
        hull(furthestPoint, rightMost, toRight, hullPoints)
    }

    private fun distance(leftMost: Point, rightMost: Point, point: Point): Int {
        val x = rightMost.x - leftMost.x
        val y = rightMost.y - leftMost.y
        val diff = x * (leftMost.y - point.y) - y * (leftMost.x - point.x)
        return Math.abs(diff)
    }

    private fun getPosition(leftMost: Point, rightMost: Point, point: Point): Int {
        val pos = (rightMost.x - leftMost.x) * (point.y - leftMost.y) - (rightMost.y - leftMost.y) * (point.x - leftMost.x)
        return pos.compareTo(0)
    }
}
