package com.example.game.platformer.levels

import com.example.game.platformer.CyclingPlatformDef
import com.example.game.platformer.Level
import com.example.game.platformer.RectObj

/** Короткая цепочка мигающих платформ с разнесёнными фазами и перекрытием по X. */
fun level36(): Level {
    val cycle = 2.2f + 2.2f
    val step = cycle / 6f
    return Level(
        startX = 30f,
        startY = -60f,
        platforms = listOf(
            RectObj(0f, 0f, 110f, 20f),
            RectObj(820f, 0f, 180f, 20f),
        ),
        hazards = emptyList(),
        checkpoints = emptyList(),
        goal = RectObj(920f, -70f, 40f, 70f),
        cyclingPlatforms = listOf(
            CyclingPlatformDef(115f, 0f, 95f, 20f, 2.2f, 2.2f, 0f),
            CyclingPlatformDef(225f, 0f, 95f, 20f, 2.2f, 2.2f, 1f * step),
            CyclingPlatformDef(335f, 0f, 95f, 20f, 2.2f, 2.2f, 2f * step),
            CyclingPlatformDef(445f, 0f, 95f, 20f, 2.2f, 2.2f, 3f * step),
            CyclingPlatformDef(555f, 0f, 95f, 20f, 2.2f, 2.2f, 4f * step),
            CyclingPlatformDef(665f, 0f, 95f, 20f, 2.2f, 2.2f, 5f * step)
        ),
        coinPickups = listOf(
            RectObj(392f, -42f, 22f, 22f),
            RectObj(612f, -42f, 22f, 22f),
        )
    )
}
