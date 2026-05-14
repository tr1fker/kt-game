package com.example.game.platformer.levels

import com.example.game.platformer.Level
import com.example.game.platformer.RectObj
import com.example.game.platformer.WindZone

fun level21(): Level = Level(
    startX = 30f,
    startY = -60f,
    platforms = listOf(
        RectObj(0f, 0f, 200f, 20f),
        RectObj(300f, -50f, 150f, 20f),
        RectObj(550f, -100f, 150f, 20f),
        RectObj(800f, -150f, 150f, 20f),
        RectObj(1050f, -200f, 300f, 20f),
    ),
    hazards = listOf(
        RectObj(200f, 50f, 1000f, 20f),
        RectObj(450f, -20f, 100f, 20f),
        RectObj(700f, -70f, 100f, 20f),
    ),
    checkpoints = listOf(
        RectObj(600f, -140f, 40f, 40f)
    ),
    goal = RectObj(1250f, -270f, 40f, 70f),
    windZones = listOf(
        WindZone(x = 200f, y = -300f, width = 1000f, height = 400f, forceX = 2500f, forceY = 0f)
    )
)
