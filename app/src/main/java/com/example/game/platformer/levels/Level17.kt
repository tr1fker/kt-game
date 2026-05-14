package com.example.game.platformer.levels

import com.example.game.platformer.Level
import com.example.game.platformer.PressurePlateDef
import com.example.game.platformer.RectObj
import com.example.game.platformer.TimedBridgeSegmentDef


fun level17(): Level = Level(
    startX = 30f,
    startY = -60f,
    platforms = listOf(
        RectObj(0f, 0f, 200f, 20f),
        RectObj(600f, 0f, 500f, 20f),
    ),
    hazards = listOf(
        RectObj(800f, -10f, 100f, 10f),
    ),
    checkpoints = emptyList(),
    goal = RectObj(1040f, -70f, 40f, 70f),
    pressurePlates = listOf(
        PressurePlateDef(
            rect = RectObj(120f, -10f, 50f, 10f),
            bridgeGroupId = 0,
            openDurationSec = 5f
        )
    ),
    timedBridgeSegments = listOf(
        TimedBridgeSegmentDef(
            groupId = 0,
            rect = RectObj(200f, 0f, 400f, 20f)
        )
    )
)
