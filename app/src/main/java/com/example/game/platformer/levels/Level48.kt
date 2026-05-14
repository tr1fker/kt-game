package com.example.game.platformer.levels

import com.example.game.platformer.Level
import com.example.game.platformer.RectObj
import com.example.game.platformer.WindZone

/** Короткий перелёт: слабый ветер по диагонали через маленькую яму. */
fun level48(): Level = Level(
    startX = 30f,
    startY = -60f,
    platforms = listOf(
        RectObj(0f, 0f, 150f, 20f),
        RectObj(380f, -80f, 620f, 20f),
    ),
    hazards = listOf(
        RectObj(150f, -12f, 230f, 10f),
    ),
    checkpoints = emptyList(),
    goal = RectObj(900f, -150f, 40f, 70f),
    windZones = listOf(
        WindZone(
            x = 155f,
            y = -140f,
            width = 200f,
            height = 120f,
            forceX = 420f,
            forceY = -520f
        )
    ),
    coinPickups = listOf(
        RectObj(260f, -122f, 22f, 22f),
        RectObj(620f, -122f, 22f, 22f),
    )
)
