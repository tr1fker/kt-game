package com.example.game.platformer.levels

import com.example.game.platformer.Level
import com.example.game.platformer.RectObj
import com.example.game.platformer.WindZone
import kotlin.collections.listOf


fun level09(): Level = Level(
    startX = 30f,
    startY = -60f,
    platforms = listOf(
        RectObj(0f, 0f, 100f, 20f),
        RectObj(200f, 100f, 300f, 20f),
        RectObj(600f, 100f, 300f, 20f),
        RectObj(480f, 120f, 20f, 100f),
        RectObj(600f, 120f, 20f, 100f),
        RectObj(480f, 220f, 140f, 20f),
    ),
    hazards = listOf(
        RectObj(300f, 90f, 100f, 10f),
        RectObj(700f, 90f, 100f, 10f),
    ),
    checkpoints = emptyList(),
    goal = RectObj(535f, 150f, 30f, 70f),
    windZones = listOf(
        WindZone(x = 200f, y = -300f, width = 700f, height = 400f, forceX = 3000f, forceY = 0f)
    ),
    coinPickups = listOf(
        RectObj(239f, 58f, 22f, 22f),
        RectObj(439f, 58f, 22f, 22f),
        RectObj(639f, 58f, 22f, 22f),
        RectObj(839f, 58f, 22f, 22f),
    )
)
