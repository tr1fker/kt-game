package com.example.game.platformer.levels

import com.example.game.platformer.CyclingPlatformDef
import com.example.game.platformer.Level
import com.example.game.platformer.RectObj
import kotlin.collections.listOf


fun level07(): Level = Level(
    startX = 30f,
    startY = -60f,
    platforms = listOf(
        RectObj(0f, 0f, 100f, 20f),
        RectObj(610f, 100f, 80f, 20f),
        RectObj(1050f, 0f, 300f, 20f),
    ),
    hazards = listOf(
        RectObj(1060f, -10f, 20f, 10f),
    ),
    checkpoints = emptyList(),
    goal = RectObj(1315f, -70f, 30f, 70f),
    cyclingPlatforms = listOf(
        CyclingPlatformDef(150f, 0f, 100f, 20f, visibleSeconds = 3f, hiddenSeconds = 3f, phaseOffsetSeconds = 0f),
        CyclingPlatformDef(300f, 0f, 100f, 20f, visibleSeconds = 3f, hiddenSeconds = 3f, phaseOffsetSeconds = 3f),
        CyclingPlatformDef(450f, 0f, 100f, 20f, visibleSeconds = 3f, hiddenSeconds = 3f, phaseOffsetSeconds = 0f),
        CyclingPlatformDef(600f, 0f, 100f, 20f, visibleSeconds = 3f, hiddenSeconds = 3f, phaseOffsetSeconds = 3f),
        CyclingPlatformDef(750f, 0f, 100f, 20f, visibleSeconds = 3f, hiddenSeconds = 3f, phaseOffsetSeconds = 0f),
        CyclingPlatformDef(900f, 0f, 100f, 20f, visibleSeconds = 3f, hiddenSeconds = 3f, phaseOffsetSeconds = 3f),
    ),
    coinPickups = listOf(
        RectObj(639f, 68f, 22f, 22f),
    )
)
