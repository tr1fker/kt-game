package com.example.game.platformer.levels

import com.example.game.platformer.Level
import com.example.game.platformer.RectObj
import com.example.game.platformer.SpringPad
import com.example.game.platformer.TeleportPair


fun level12(): Level = Level(
    startX = 30f,
    startY = -60f,
    platforms = listOf(
        RectObj(0f, 0f, 100f, 20f),
        RectObj(0f, -400f, 100f, 20f),
        RectObj(500f, -400f, 500f, 20f),
        RectObj(900f, -470f, 20f, 70f),
    ),
    hazards = listOf(
        RectObj(700f, -410f, 100f, 10f),
    ),
    checkpoints = emptyList(),
    goal = RectObj(950f, -470f, 30f, 70f),
    springPads = listOf(
        SpringPad(RectObj(150f, -100f, 70f, 20f), jumpImpulse = -800f),
        SpringPad(RectObj(-150f, -300f, 70f, 20f), jumpImpulse = -800f),
    ),
    teleports = listOf(
        TeleportPair(
            from = RectObj(35f, -470f, 30f, 70f),
            to = RectObj(535f, -470f, 30f, 70f)
        )
    ),
    coinPickups = listOf(
        RectObj(739f, -500f, 22f, 22f),
    )
)
