package com.example.game.platformer.levels

import com.example.game.platformer.Level
import com.example.game.platformer.RectObj
import com.example.game.platformer.WindZone


fun level10(): Level = Level(
    startX = 30f,
    startY = -60f,
    platforms = listOf(
        RectObj(0f, 0f, 300f, 20f),
        RectObj(600f, 200f, 100f, 20f),
        RectObj(600f, -200f, 500f, 20f),
    ),
    hazards = listOf(
        RectObj(700f, -210f, 100f, 10f),
    ),
    checkpoints = emptyList(),
    goal = RectObj(1065f, -270f, 30f, 70f),
    windZones = listOf(
        WindZone(x = 300f, y = -500f, width = 300f, height = 1000f, forceX = 0f, forceY = -1000f)
    ),
    coinPickups = listOf(
        RectObj(607f, 158f, 22f, 22f),
        RectObj(636f, 158f, 22f, 22f),
        RectObj(665f, 158f, 22f, 22f),
    )
)
