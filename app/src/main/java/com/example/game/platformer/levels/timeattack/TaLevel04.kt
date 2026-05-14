package com.example.game.platformer.levels.timeattack

import com.example.game.platformer.CyclingPlatformDef
import com.example.game.platformer.Level
import com.example.game.platformer.RectObj


fun taLevel04(): Level = Level(
    startX = 30f,
    startY = -60f,
    platforms = listOf(
        RectObj(0f, 0f, 800f, 20f),
    ),
    hazards = listOf(
        RectObj(120f, -10f, 50f, 10f),
        RectObj(260f, -10f, 50f, 10f),
        RectObj(400f, -10f, 50f, 10f),
        RectObj(540f, -10f, 50f, 10f),
    ),
    checkpoints = emptyList(),
    goal = RectObj(740f, -70f, 40f, 70f),
    visibilityRadiusWorld = 100f,
    timeLimitSeconds = 15
)
