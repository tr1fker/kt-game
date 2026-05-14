package com.example.game.platformer.levels

import com.example.game.platformer.CyclingHazardDef
import com.example.game.platformer.CyclingPlatformDef
import com.example.game.platformer.Level
import com.example.game.platformer.RectObj

fun level25(): Level = Level(
    startX = 30f,
    startY = -60f,
    platforms = listOf(
        RectObj(0f, 0f, 250f, 20f),
    ),
    cyclingPlatforms = listOf(
        CyclingPlatformDef(300f, -50f, 150f, 20f, 1.8f, 1.2f),
        CyclingPlatformDef(500f, -100f, 150f, 20f, 1.8f, 1.2f, phaseOffsetSeconds = 0.6f),
        CyclingPlatformDef(700f, -150f, 150f, 20f, 1.8f, 1.2f, phaseOffsetSeconds = 1.2f),
        CyclingPlatformDef(900f, -200f, 150f, 20f, 1.8f, 1.2f, phaseOffsetSeconds = 1.8f),
    ),
    cyclingHazards = listOf(
        CyclingHazardDef(450f, -20f, 50f, 20f, 1.0f, 1.0f),
        CyclingHazardDef(650f, -70f, 50f, 20f, 1.0f, 1.0f, phaseOffsetSeconds = 0.5f),
        CyclingHazardDef(850f, -120f, 50f, 20f, 1.0f, 1.0f, phaseOffsetSeconds = 1.0f),
    ),
    hazards = listOf(
        RectObj(-200f, 150f, 1500f, 40f)
    ),
    checkpoints = listOf(
        RectObj(600f, -140f, 40f, 40f)
    ),
    goal = RectObj(1100f, -270f, 40f, 70f)
)
