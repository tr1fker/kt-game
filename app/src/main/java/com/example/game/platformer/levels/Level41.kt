package com.example.game.platformer.levels

import com.example.game.platformer.Level
import com.example.game.platformer.RectObj

/** Два ледяных куска и сухой мост между ними (мост не в slipperyPlatforms). */
fun level41(): Level {
    val iceA = RectObj(0f, 0f, 200f, 20f)
    val iceB = RectObj(420f, -65f, 220f, 20f)
    val bridge = RectObj(200f, -32f, 220f, 18f)
    return Level(
        startX = 40f,
        startY = -60f,
        platforms = listOf(iceA, bridge, iceB, RectObj(720f, 0f, 280f, 20f)),
        hazards = emptyList(),
        checkpoints = emptyList(),
        goal = RectObj(920f, -70f, 40f, 70f),
        slipperyPlatforms = listOf(iceA, iceB),
        slipperyGroundVelocityRetainPerTick = 0.97f,
        slipperyMaxHorizontalSpeedFactor = 1.32f,
        coinPickups = listOf(
            RectObj(99f, -42f, 22f, 22f),
            RectObj(299f, -72f, 22f, 22f),
            RectObj(819f, -42f, 22f, 22f),
        )
    )
}
