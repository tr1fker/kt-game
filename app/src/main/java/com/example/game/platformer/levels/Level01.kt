package com.example.game.platformer.levels

import com.example.game.platformer.Level
import com.example.game.platformer.RectObj


fun level01(): Level = Level(
    startX = 30f,
    startY = -30f,
    platforms = listOf(
        RectObj(0f, 0f, 280f, 20f),
        RectObj(-20f, -180f, 20f, 200f),
        RectObj(280f, 0f, 20f, 150f),
        RectObj(300f, 80f, 500f, 20f),
        RectObj(780f, -20f, 20f, 100f),
        RectObj(800f, -20f, 100f, 20f),
        RectObj(880f, -220f, 20f, 200f),
        RectObj(780f, -240f, 120f, 20f),
        RectObj(680f, -150f, 100f, 20f),
        RectObj(980f, -420f, 20f, 500f),
        RectObj(880f, 60f, 100f, 20f),
        RectObj(880f, 80f, 20f, 150f),
        RectObj(80f, 230f, 820f, 20f),
        RectObj(180f, 130f, 100f, 20f),
        RectObj(80f, 20f, 20f, 210f),
    ),
    hazards = emptyList(),
    checkpoints = emptyList(),
    goal = RectObj(215f, 60f, 30f, 70f)
)
