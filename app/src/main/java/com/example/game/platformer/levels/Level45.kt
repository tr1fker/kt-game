package com.example.game.platformer.levels

import com.example.game.platformer.CyclingPlatformDef
import com.example.game.platformer.Level
import com.example.game.platformer.RectObj

/** Пять мигающих платформ: фазы равномерно по циклу, перекрытие 18 px. */
fun level45(): Level {
    val cycle = 2.0f + 2.0f
    val step = cycle / 6f
    return Level(
        startX = 30f,
        startY = -60f,
        platforms = listOf(
            RectObj(0f, 0f, 100f, 20f),
            RectObj(800f, 0f, 200f, 20f),
        ),
        hazards = emptyList(),
        checkpoints = emptyList(),
        goal = RectObj(920f, -70f, 40f, 70f),
        cyclingPlatforms = listOf(
            CyclingPlatformDef(105f, 0f, 92f, 20f, 2f, 2f, 0f),
            CyclingPlatformDef(212f, 0f, 92f, 20f, 2f, 2f, 1f * step),
            CyclingPlatformDef(319f, 0f, 92f, 20f, 2f, 2f, 2f * step),
            CyclingPlatformDef(426f, 0f, 92f, 20f, 2f, 2f, 3f * step),
            CyclingPlatformDef(533f, 0f, 92f, 20f, 2f, 2f, 4f * step),
            CyclingPlatformDef(640f, 0f, 92f, 20f, 2f, 2f, 5f * step)
        ),
        coinPickups = listOf(RectObj(420f, -42f, 22f, 22f))
    )
}
