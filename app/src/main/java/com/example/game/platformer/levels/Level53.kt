package com.example.game.platformer.levels

import com.example.game.platformer.Level
import com.example.game.platformer.RectObj
import com.example.game.platformer.SpringPad

/** Три пружины на ступенях, шипы у краёв промежутков. */
fun level53(): Level = Level(
    startX = 30f,
    startY = -60f,
    platforms = listOf(
        RectObj(0f, 0f, 140f, 20f),
        RectObj(280f, -110f, 110f, 20f),
        RectObj(520f, -220f, 110f, 20f),
        RectObj(760f, -320f, 240f, 20f),
    ),
    hazards = listOf(
        RectObj(145f, -12f, 120f, 10f),
        RectObj(385f, -12f, 120f, 10f),
    ),
    checkpoints = emptyList(),
    goal = RectObj(920f, -390f, 40f, 70f),
    springPads = listOf(
        SpringPad(RectObj(55f, -12f, 64f, 18f), jumpImpulse = -625f),
        SpringPad(RectObj(315f, -122f, 64f, 18f), jumpImpulse = -645f),
        SpringPad(RectObj(555f, -232f, 64f, 18f), jumpImpulse = -665f),
    ),
    coinPickups = listOf(
        RectObj(99f, -42f, 22f, 22f),
        RectObj(569f, -262f, 22f, 22f),
    )
)
