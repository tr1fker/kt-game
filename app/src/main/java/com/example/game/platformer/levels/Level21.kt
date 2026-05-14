package com.example.game.platformer.levels

import com.example.game.platformer.Level
import com.example.game.platformer.RectObj
import com.example.game.platformer.WindZone

fun level21(): Level = Level(
    startX = 30f,
    startY = -60f,
    platforms = listOf(
        RectObj(0f, 0f, 300f, 20f),
        RectObj(400f, -80f, 300f, 20f),
        RectObj(800f, -160f, 300f, 20f),
        RectObj(1200f, -240f, 300f, 20f),
        RectObj(1600f, -320f, 400f, 20f),
    ),
    hazards = listOf(
        RectObj(300f, 100f, 1500f, 40f),
    ),
    checkpoints = listOf(
        RectObj(950f, -200f, 40f, 40f)
    ),
    goal = RectObj(1800f, -390f, 40f, 70f),
    windZones = listOf(
        WindZone(x = 300f, y = -500f, width = 1500f, height = 600f, forceX = 1500f, forceY = 0f)
    )
)