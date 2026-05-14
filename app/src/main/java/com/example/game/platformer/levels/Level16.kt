package com.example.game.platformer.levels

import com.example.game.platformer.CyclingHazardDef
import com.example.game.platformer.Level
import com.example.game.platformer.RectObj


fun level16(): Level = Level(
    startX = 30f,
    startY = -60f,
    platforms = listOf(
        RectObj(0f, 0f, 1200f, 20f),
        RectObj(0f, -300f, 1200f, 20f),
    ),
    hazards = emptyList(),
    checkpoints = emptyList(),
    goal = RectObj(1140f, -70f, 40f, 70f),
    cyclingHazards = listOf(
        CyclingHazardDef(
            x = 100f,
            y = -280f,
            width = 20f,
            height = 280f,
            visibleSeconds = 1.52f,
            hiddenSeconds = 1.5f,
            phaseOffsetSeconds = 0f,
            fadeInSeconds = 0.1f,
            fadeOutSeconds = 0.12f
        ),
        CyclingHazardDef(
            x = 240f,
            y = -280f,
            width = 20f,
            height = 280f,
            visibleSeconds = 2f,
            hiddenSeconds = 1.5f,
            phaseOffsetSeconds = 0f,
            fadeInSeconds = 0.1f,
            fadeOutSeconds = 0.12f
        ),
        CyclingHazardDef(
            x = 380f,
            y = -280f,
            width = 20f,
            height = 280f,
            visibleSeconds = 1.52f,
            hiddenSeconds = 1.5f,
            phaseOffsetSeconds = 1f,
            fadeInSeconds = 0.1f,
            fadeOutSeconds = 0.12f
        ),
        CyclingHazardDef(
            x = 520f,
            y = -280f,
            width = 20f,
            height = 280f,
            visibleSeconds = 2f,
            hiddenSeconds = 1.5f,
            phaseOffsetSeconds = 1f,
            fadeInSeconds = 0.1f,
            fadeOutSeconds = 0.12f
        ),
        CyclingHazardDef(
            x = 660f,
            y = -280f,
            width = 20f,
            height = 280f,
            visibleSeconds = 1.52f,
            hiddenSeconds = 1.5f,
            phaseOffsetSeconds = 0f,
            fadeInSeconds = 0.1f,
            fadeOutSeconds = 0.12f
        ),
        CyclingHazardDef(
            x = 800f,
            y = -280f,
            width = 20f,
            height = 280f,
            visibleSeconds = 2f,
            hiddenSeconds = 1.5f,
            phaseOffsetSeconds = 0f,
            fadeInSeconds = 0.1f,
            fadeOutSeconds = 0.12f
        ),
        CyclingHazardDef(
            x = 940f,
            y = -280f,
            width = 20f,
            height = 280f,
            visibleSeconds = 1.52f,
            hiddenSeconds = 1.5f,
            phaseOffsetSeconds = 1f,
            fadeInSeconds = 0.1f,
            fadeOutSeconds = 0.12f
        ),
        CyclingHazardDef(
            x = 1080f,
            y = -280f,
            width = 20f,
            height = 280f,
            visibleSeconds = 2f,
            hiddenSeconds = 1.5f,
            phaseOffsetSeconds = 1f,
            fadeInSeconds = 0.1f,
            fadeOutSeconds = 0.12f
        ),
    ),
    coinPickups = listOf(
        RectObj(-50f, -80f, 22f, 22f),
    )
)
