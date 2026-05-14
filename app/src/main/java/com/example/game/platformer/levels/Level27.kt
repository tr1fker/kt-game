package com.example.game.platformer.levels

import com.example.game.platformer.Level
import com.example.game.platformer.RectObj
import com.example.game.platformer.SpringPad

fun level27(): Level = Level(
    startX = 30f,
    startY = -60f,
    platforms = listOf(
        RectObj(0f, 0f, 200f, 20f),
        RectObj(400f, -200f, 200f, 20f),
        RectObj(800f, -400f, 200f, 20f),
        RectObj(1200f, -600f, 400f, 20f),
    ),
    springPads = listOf(
        SpringPad(RectObj(100f, -20f, 40f, 20f)),
        SpringPad(RectObj(500f, -220f, 40f, 20f)),
        SpringPad(RectObj(900f, -420f, 40f, 20f)),
    ),
    hazards = listOf(
        RectObj(200f, 50f, 1400f, 20f),
        RectObj(300f, -150f, 100f, 20f),
        RectObj(700f, -350f, 100f, 20f),
    ),
    checkpoints = listOf(
        RectObj(450f, -240f, 40f, 40f)
    ),
    goal = RectObj(1500f, -670f, 40f, 70f)
)