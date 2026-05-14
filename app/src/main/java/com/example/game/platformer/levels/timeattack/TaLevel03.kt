package com.example.game.platformer.levels.timeattack

import com.example.game.platformer.CyclingPlatformDef
import com.example.game.platformer.Level
import com.example.game.platformer.RectObj


fun taLevel03(): Level = Level(
    startX = 30f,
    startY = -60f,
    platforms = listOf(
        RectObj(0f, 0f, 100f, 20f),
        RectObj(130f, -100f, 80f, 20f),
        RectObj(20f, -200f, 60f, 20f),
        RectObj(110f, -300f, 40f, 20f),
        RectObj(40f, -400f, 20f, 20f),
        RectObj(130f, -500f, 220f, 20f),
        RectObj(330f, -480f, 20f, 300f),
        RectObj(350f, -300f, 100f, 20f),
        RectObj(350f, -200f, 100f, 20f),
    ),
    hazards = emptyList(),
    checkpoints = emptyList(),
    goal = RectObj(385f, -270f, 30f, 70f),
    coinPickups = listOf(
        RectObj(39f, -650f, 22f, 22f),
    ),
    timeLimitSeconds = 15
)
