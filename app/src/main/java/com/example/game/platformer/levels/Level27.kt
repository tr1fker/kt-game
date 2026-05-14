package com.example.game.platformer.levels

import com.example.game.platformer.Level
import com.example.game.platformer.RectObj
import com.example.game.platformer.SpringPad

fun level27(): Level = Level(
    startX = 30f,
    startY = -60f,
    platforms = listOf(
        RectObj(0f, 0f, 250f, 20f),
        RectObj(450f, -200f, 250f, 20f),
        RectObj(900f, -400f, 250f, 20f),
        RectObj(1350f, -600f, 450f, 20f),
    ),
    springPads = listOf(
        SpringPad(RectObj(100f, -20f, 60f, 20f), jumpImpulse = -950f),
        SpringPad(RectObj(550f, -220f, 60f, 20f), jumpImpulse = -950f),
        SpringPad(RectObj(1000f, -420f, 60f, 20f), jumpImpulse = -950f),
    ),
    hazards = listOf(
        RectObj(-200f, 150f, 2500f, 40f),
        RectObj(350f, -150f, 100f, 20f),
        RectObj(800f, -350f, 100f, 20f),
        RectObj(1250f, -550f, 100f, 20f),
    ),
    checkpoints = listOf(
        RectObj(550f, -240f, 40f, 40f),
        RectObj(1000f, -440f, 40f, 40f)
    ),
    goal = RectObj(1700f, -670f, 40f, 70f)
)
