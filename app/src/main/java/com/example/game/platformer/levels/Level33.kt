package com.example.game.platformer.levels

import com.example.game.platformer.Level
import com.example.game.platformer.PressurePlateDef
import com.example.game.platformer.RectObj
import com.example.game.platformer.TimedBridgeSegmentDef

/** Плита открывает мост через разрыв (достаточно времени, чтобы спокойно пройти). */
fun level33(): Level = Level(
    startX = 30f,
    startY = -60f,
    platforms = listOf(
        RectObj(0f, 0f, 170f, 20f),
        RectObj(770f, 0f, 230f, 20f),
    ),
    hazards = emptyList(),
    checkpoints = emptyList(),
    goal = RectObj(920f, -70f, 40f, 70f),
    pressurePlates = listOf(
        PressurePlateDef(
            rect = RectObj(95f, -10f, 52f, 10f),
            bridgeGroupId = 0,
            openDurationSec = 7f
        )
    ),
    timedBridgeSegments = listOf(
        TimedBridgeSegmentDef(
            groupId = 0,
            rect = RectObj(170f, 0f, 600f, 20f)
        )
    ),
    coinPickups = listOf(
        RectObj(420f, -42f, 22f, 22f),
    )
)
