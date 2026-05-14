package com.example.game.platformer.levels

import com.example.game.platformer.Level
import com.example.game.platformer.RectObj
import com.example.game.platformer.TeleportPair

/** Два телепорта подряд: снизу вверх по ярусы. */
fun level43(): Level = Level(
    startX = 30f,
    startY = -60f,
    platforms = listOf(
        RectObj(0f, 0f, 130f, 20f),
        RectObj(380f, -140f, 200f, 20f),
        RectObj(760f, -280f, 240f, 20f),
    ),
    hazards = listOf(
        RectObj(140f, -12f, 220f, 10f),
    ),
    checkpoints = emptyList(),
    goal = RectObj(920f, -350f, 40f, 70f),
    teleports = listOf(
        TeleportPair(
            from = RectObj(85f, -70f, 32f, 70f),
            to = RectObj(430f, -210f, 34f, 70f)
        ),
        TeleportPair(
            from = RectObj(470f, -210f, 32f, 70f),
            to = RectObj(820f, -350f, 36f, 70f)
        )
    ),
    coinPickups = listOf(
        RectObj(48f, -42f, 22f, 22f),
        RectObj(840f, -322f, 22f, 22f),
    )
)
