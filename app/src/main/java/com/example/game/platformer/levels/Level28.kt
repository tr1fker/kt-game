package com.example.game.platformer.levels

import com.example.game.platformer.Level
import com.example.game.platformer.RectObj

fun level28(): Level = Level(
    startX = 30f,
    startY = -60f,
    platforms = listOf(
        RectObj(0f, 0f, 200f, 20f),
        RectObj(250f, -100f, 50f, 20f),
        RectObj(100f, -200f, 50f, 20f),
        RectObj(250f, -300f, 50f, 20f),
        RectObj(100f, -400f, 50f, 20f),
        RectObj(250f, -500f, 50f, 20f),
        RectObj(100f, -600f, 50f, 20f),
        RectObj(0f, -750f, 400f, 20f),
    ),
    hazards = listOf(
        RectObj(-100f, 50f, 600f, 20f),
        RectObj(0f, -700f, 50f, 20f),
        RectObj(350f, -700f, 50f, 20f),
    ),
    checkpoints = listOf(
        RectObj(130f, -440f, 40f, 40f)
    ),
    goal = RectObj(180f, -820f, 40f, 70f),
    visibilityRadiusWorld = 250f
)