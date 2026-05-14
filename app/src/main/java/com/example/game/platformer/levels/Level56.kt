package com.example.game.platformer.levels

import com.example.game.platformer.Level
import com.example.game.platformer.RectObj

/** Туман и таймер: только ступени, без шипов (ориентир по времени, не по ловушкам). */
fun level56(): Level = Level(
    startX = 30f,
    startY = -60f,
    platforms = listOf(
        RectObj(0f, 0f, 160f, 20f),
        RectObj(220f, -60f, 120f, 20f),
        RectObj(400f, -120f, 120f, 20f),
        RectObj(580f, -180f, 120f, 20f),
        RectObj(760f, -240f, 240f, 20f),
    ),
    hazards = emptyList(),
    checkpoints = listOf(
        RectObj(620f, -180f, 40f, 70f)
    ),
    goal = RectObj(920f, -310f, 40f, 70f),
    timeLimitSeconds = 72,
    visibilityRadiusWorld = 122f,
    coinPickups = listOf(
        RectObj(109f, -42f, 22f, 22f),
        RectObj(649f, -222f, 22f, 22f),
    )
)
