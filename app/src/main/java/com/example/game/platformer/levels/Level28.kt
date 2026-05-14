package com.example.game.platformer.levels

import com.example.game.platformer.Level
import com.example.game.platformer.RectObj

fun level28(): Level = Level(
    startX = 30f,
    startY = -60f,
    platforms = listOf(
        RectObj(0f, 0f, 250f, 20f),
        RectObj(350f, -110f, 150f, 20f),
        RectObj(100f, -220f, 150f, 20f),
        RectObj(350f, -330f, 150f, 20f),
        RectObj(100f, -440f, 150f, 20f),
        RectObj(350f, -550f, 150f, 20f),
        RectObj(100f, -660f, 150f, 20f),
        RectObj(0f, -770f, 550f, 20f),
    ),
    hazards = listOf(
        RectObj(-200f, 150f, 1000f, 40f),
        // Hazards that were blocking the final jump have been removed
    ),
    checkpoints = listOf(
        RectObj(150f, -240f, 40f, 40f),
        RectObj(400f, -570f, 40f, 40f)
    ),
    goal = RectObj(250f, -840f, 40f, 70f),
    visibilityRadiusWorld = 160f
)
