package com.example.game.platformer.levels

import com.example.game.platformer.Level
import com.example.game.platformer.RectObj

/** Туман + таймер, простая лесенка без шипов в дуге прыжка. */
fun level42(): Level = Level(
    startX = 30f,
    startY = -60f,
    platforms = listOf(
        RectObj(0f, 0f, 170f, 20f),
        RectObj(240f, -70f, 130f, 20f),
        RectObj(440f, -140f, 130f, 20f),
        RectObj(640f, -210f, 360f, 20f),
    ),
    hazards = emptyList(),
    checkpoints = listOf(
        RectObj(720f, -210f, 40f, 70f)
    ),
    goal = RectObj(920f, -280f, 40f, 70f),
    timeLimitSeconds = 78,
    visibilityRadiusWorld = 128f,
    coinPickups = listOf(
        RectObj(119f, -42f, 22f, 22f),
        RectObj(489f, -182f, 22f, 22f),
        RectObj(820f, -252f, 22f, 22f),
    )
)
