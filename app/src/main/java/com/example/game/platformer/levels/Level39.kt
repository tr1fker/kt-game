package com.example.game.platformer.levels

import com.example.game.platformer.Level
import com.example.game.platformer.RectObj
import com.example.game.platformer.WindZone

/** Узкий восходящий поток + промежуточная полка (как идея 21, короче по горизонтали). */
fun level39(): Level = Level(
    startX = 30f,
    startY = -60f,
    platforms = listOf(
        RectObj(0f, 0f, 140f, 20f),
        RectObj(280f, -180f, 140f, 20f),
        RectObj(520f, -220f, 480f, 20f),
    ),
    hazards = listOf(
        RectObj(140f, -12f, 140f, 10f),
    ),
    checkpoints = emptyList(),
    goal = RectObj(900f, -290f, 40f, 70f),
    windZones = listOf(
        WindZone(
            x = 135f,
            y = -320f,
            width = 150f,
            height = 360f,
            forceX = 260f,
            forceY = -1180f
        )
    ),
    coinPickups = listOf(
        RectObj(340f, -222f, 22f, 22f),
        RectObj(700f, -262f, 22f, 22f),
    )
)
