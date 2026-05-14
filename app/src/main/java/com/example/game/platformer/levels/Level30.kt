package com.example.game.platformer.levels

import com.example.game.platformer.Level
import com.example.game.platformer.RectObj
import com.example.game.platformer.WindZone

/** Горизонтальный порыв ветра через яму с шипами — посадка на длинный пол справа. */
fun level30(): Level = Level(
    startX = 30f,
    startY = -60f,
    platforms = listOf(
        RectObj(0f, 0f, 130f, 20f),
        RectObj(400f, 0f, 600f, 20f),
    ),
    hazards = listOf(
        RectObj(130f, -12f, 270f, 10f),
    ),
    checkpoints = emptyList(),
    goal = RectObj(920f, -70f, 40f, 70f),
    windZones = listOf(
        WindZone(
            x = 125f,
            y = -130f,
            width = 240f,
            height = 130f,
            forceX = 1050f,
            forceY = -220f
        )
    ),
    coinPickups = listOf(
        RectObj(520f, -42f, 22f, 22f),
        RectObj(780f, -42f, 22f, 22f),
    )
)
