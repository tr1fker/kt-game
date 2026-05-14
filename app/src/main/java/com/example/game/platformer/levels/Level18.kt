package com.example.game.platformer.levels

import com.example.game.platformer.Level
import com.example.game.platformer.RectObj


fun level18(): Level = Level(
    startX = 30f,
    startY = -60f,
    platforms = listOf(
        RectObj(0f, 0f, 1000f, 20f),
        RectObj(100f, -100f, 100f, 20f),
        RectObj(300f, -100f, 100f, 20f),
        RectObj(500f, -100f, 100f, 20f),
    ),
    hazards = emptyList(),
    checkpoints = listOf(
        RectObj(940f, -70f, 40f, 70f)
    ),
    goal = RectObj(1290f, -70f, 40f, 70f),
    coinPickups = listOf(
        RectObj(139f, -142f, 22f, 22f),
        RectObj(139f, -42f, 22f, 22f),
        RectObj(339f, -142f, 22f, 22f),
        RectObj(339f, -42f, 22f, 22f),
        RectObj(539f, -142f, 22f, 22f),
        RectObj(539f, -42f, 22f, 22f),
    )
)
