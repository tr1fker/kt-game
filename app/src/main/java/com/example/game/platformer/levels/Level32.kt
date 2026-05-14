package com.example.game.platformer.levels

import com.example.game.platformer.Level
import com.example.game.platformer.RectObj
import com.example.game.platformer.TeleportPair

/** Короткий маршрут: телепорт с нижнего левого берега на верхний правый. */
fun level32(): Level = Level(
    startX = 30f,
    startY = -60f,
    platforms = listOf(
        RectObj(0f, 0f, 150f, 20f),
        RectObj(740f, -200f, 260f, 20f),
    ),
    hazards = listOf(
        RectObj(200f, -12f, 500f, 10f),
    ),
    checkpoints = emptyList(),
    goal = RectObj(920f, -270f, 40f, 70f),
    teleports = listOf(
        TeleportPair(
            from = RectObj(95f, -70f, 34f, 70f),
            to = RectObj(800f, -270f, 36f, 70f)
        )
    ),
    coinPickups = listOf(
        RectObj(48f, -42f, 22f, 22f),
        RectObj(860f, -242f, 22f, 22f),
    )
)
