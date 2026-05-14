package com.example.game.platformer.levels

import com.example.game.platformer.Level
import com.example.game.platformer.RectObj


fun level08(): Level {
    val ice01 = RectObj(0f, 0f, 400f, 20f)
    val ice02 = RectObj(550f, -50f, 400f, 20f)
    val ice03 = RectObj(1100f, 0f, 400f, 20f)
    val ice04 = RectObj(1650f, 50f, 400f, 20f)
    val ice05 = RectObj(2200f, 0f, 400f, 20f)
    val ice06 = RectObj(2750f, -50f, 400f, 20f)
    val ice07 = RectObj(3300f, 0f, 400f, 20f)
    return Level(
        startX = 180f,
        startY = -60f,
        platforms = listOf(
            ice01, ice02, ice03, ice04, ice05, ice06, ice07,
            RectObj(3850f, 0f, 200f, 20f)
        ),
        hazards = listOf(

        ),
        checkpoints = emptyList(),
        goal = RectObj(4000f, -70f, 30f, 70f),
        slipperyPlatforms = listOf(
            ice01, ice02, ice03, ice04, ice05, ice06, ice07
        ),
        slipperyGroundVelocityRetainPerTick = 1.5f,
        slipperyMaxHorizontalSpeedFactor = 1.5f,
        coinPickups = listOf(
            RectObj(378f, -42f, 22f, 22f),
            RectObj(928f, -92f, 22f, 22f),
            RectObj(1478f, -42f, 22f, 22f),
            RectObj(2028f, 8f, 22f, 22f),
            RectObj(2578f, -42f, 22f, 22f),
            RectObj(3128f, -92f, 22f, 22f),
            RectObj(3678f, -42f, 22f, 22f),
        )
    )
}
