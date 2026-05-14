package com.example.game.platformer.levels

import com.example.game.platformer.Level
import com.example.game.platformer.RectObj
import com.example.game.platformer.SpringPad

/** Пружины на ступенях; шипы только у края ямы, не под центром зазора. */
fun level44(): Level = Level(
    startX = 30f,
    startY = -60f,
    platforms = listOf(
        RectObj(0f, 0f, 150f, 20f),
        RectObj(260f, -100f, 120f, 20f),
        RectObj(500f, -200f, 120f, 20f),
        RectObj(740f, -300f, 260f, 20f),
    ),
    hazards = listOf(
        RectObj(155f, -12f, 90f, 10f),
        RectObj(395f, -12f, 90f, 10f),
    ),
    checkpoints = emptyList(),
    goal = RectObj(920f, -370f, 40f, 70f),
    springPads = listOf(
        SpringPad(RectObj(65f, -12f, 66f, 18f), jumpImpulse = -630f),
        SpringPad(RectObj(305f, -112f, 66f, 18f), jumpImpulse = -650f),
        SpringPad(RectObj(545f, -212f, 66f, 18f), jumpImpulse = -670f),
    ),
    coinPickups = listOf(
        RectObj(109f, -42f, 22f, 22f),
        RectObj(549f, -242f, 22f, 22f),
    )
)
