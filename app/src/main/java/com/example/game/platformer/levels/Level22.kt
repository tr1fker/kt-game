package com.example.game.platformer.levels

import com.example.game.platformer.Level
import com.example.game.platformer.RectObj

fun level22(): Level {
    val p1 = RectObj(0f, 0f, 200f, 20f)
    val s1 = RectObj(250f, -110f, 200f, 20f)
    val s2 = RectObj(0f, -220f, 200f, 20f)
    val s3 = RectObj(250f, -330f, 200f, 20f)
    val s4 = RectObj(0f, -440f, 200f, 20f)
    val s5 = RectObj(250f, -550f, 200f, 20f)
    val s6 = RectObj(0f, -660f, 450f, 20f)

    return Level(
        startX = 30f,
        startY = -60f,
        platforms = listOf(p1, s1, s2, s3, s4, s5, s6),
        slipperyPlatforms = listOf(s1, s2, s3, s4, s5, s6),
        hazards = listOf(
            RectObj(-200f, 150f, 1000f, 40f),
        ),
        checkpoints = listOf(
            RectObj(100f, -260f, 40f, 40f),
            RectObj(100f, -480f, 40f, 40f)
        ),
        goal = RectObj(200f, -730f, 40f, 70f)
    )
}