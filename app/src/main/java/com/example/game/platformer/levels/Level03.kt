package com.example.game.platformer.levels

import com.example.game.platformer.Level
import com.example.game.platformer.RectObj


fun level03(): Level = Level(
    startX = 20f,
    startY = -60f,
    platforms = listOf(
        RectObj(0f, 0f, 1000f, 20f),
        RectObj(600f, -100f, 400f, 20f),
        RectObj(600f, -200f, 20f, 100f),
        RectObj(1100f, 0f, 20f, 200f),
        RectObj(600f, 180f, 500f, 20f),
    ),
    hazards = listOf(
        RectObj(100f, -10f, 50f, 10f),
        RectObj(250f, -10f, 70f, 10f),
        RectObj(400f, -10f, 90f, 10f),
        RectObj(610f, -80f, 380f, 10f),
    ),
    checkpoints = emptyList(),
    goal = RectObj(750f, 110f, 30f, 70f),
    coinPickups = listOf(
        RectObj(630f, -142f, 22f, 22f),
        RectObj(670f, -142f, 22f, 22f),
        RectObj(710f, -142f, 22f, 22f),
        RectObj(708f, 140f, 22f, 22f),
    )
)
