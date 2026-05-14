package com.example.game.platformer.levels

import com.example.game.platformer.CyclingHazardDef
import com.example.game.platformer.CyclingPlatformDef
import com.example.game.platformer.Level
import com.example.game.platformer.RectObj

fun level25(): Level = Level(
    startX = 30f,
    startY = -60f,
    platforms = listOf(
        RectObj(0f, 0f, 200f, 20f),
    ),
    cyclingPlatforms = listOf(
        CyclingPlatformDef(250f, -50f, 100f, 20f, 1.5f, 1.5f),
        CyclingPlatformDef(400f, -100f, 100f, 20f, 1.5f, 1.5f, phaseOffsetSeconds = 0.5f),
        CyclingPlatformDef(550f, -150f, 100f, 20f, 1.5f, 1.5f, phaseOffsetSeconds = 1.0f),
        CyclingPlatformDef(700f, -200f, 100f, 20f, 1.5f, 1.5f, phaseOffsetSeconds = 1.5f),
    ),
    cyclingHazards = listOf(
        CyclingHazardDef(350f, -30f, 50f, 20f, 1.0f, 1.0f),
        CyclingHazardDef(500f, -80f, 50f, 20f, 1.0f, 1.0f, phaseOffsetSeconds = 0.5f),
        CyclingHazardDef(650f, -130f, 50f, 20f, 1.0f, 1.0f, phaseOffsetSeconds = 1.0f),
    ),
    hazards = listOf(
        RectObj(200f, 50f, 800f, 20f)
    ),
    checkpoints = emptyList(),
    goal = RectObj(850f, -270f, 40f, 70f)
)