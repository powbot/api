package org.powbot.walking.local

object Flag {

    const val W_NW = 1
    const val W_N = 2
    const val W_NE = 4
    const val W_E = 8
    const val W_SE = 16
    const val W_S = 32
    const val W_SW = 0x1000000
    const val W_W = 128

    const val BLOCKED = 256
    const val BLOCKED2 = 2097152
    const val BLOCKED4 = 262144

    const val WATER = 19398912

    object Rotation {
        const val DIAGONAL = 0x1
        const val WEST = 0x0
        const val NORTH = 0x1
        const val EAST = 0x2
        const val SOUTH = 0x3
    }

}
