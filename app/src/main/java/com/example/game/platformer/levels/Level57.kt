package com.example.game.platformer.levels

import com.example.game.platformer.Level
import com.example.game.platformer.RectObj
import com.example.game.platformer.TeleportPair

/** Телепорт через широкую яму на верхний берег. */
fun level57(): Level = Level(
    startX = 30f,
    startY = -60f,
    platforms = listOf(
        RectObj(0f, 0f, 140f, 20f),
        RectObj(720f, -200f, 280f, 20f),
    ),
    hazards = listOf(
        RectObj(140f, -12f, 580f, 10f),
    ),
    checkpoints = emptyList(),
    goal = RectObj(920f, -270f, 40f, 70f),
    teleports = listOf(
        TeleportPair(
            from = RectObj(90f, -70f, 34f, 70f),
            to = RectObj(780f, -270f, 36f, 70f)
        )
    ),
    coinPickups = listOf(
        RectObj(48f, -42f, 22f, 22f),
        RectObj(820f, -242f, 22f, 22f),
    )
)
