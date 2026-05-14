package com.example.game.platformer.levels

import com.example.game.platformer.Level
import com.example.game.platformer.PressurePlateDef
import com.example.game.platformer.RectObj
import com.example.game.platformer.TimedBridgeSegmentDef

/** Две плиты на один мост (обе продлевают время — можно наступить с любого берега). */
fun level47(): Level = Level(
    startX = 30f,
    startY = -60f,
    platforms = listOf(
        RectObj(0f, 0f, 140f, 20f),
        RectObj(800f, 0f, 200f, 20f),
    ),
    hazards = emptyList(),
    checkpoints = emptyList(),
    goal = RectObj(920f, -70f, 40f, 70f),
    pressurePlates = listOf(
        PressurePlateDef(
            rect = RectObj(70f, -10f, 48f, 10f),
            bridgeGroupId = 0,
            openDurationSec = 6.5f
        ),
        PressurePlateDef(
            rect = RectObj(820f, -10f, 48f, 10f),
            bridgeGroupId = 0,
            openDurationSec = 6.5f
        )
    ),
    timedBridgeSegments = listOf(
        TimedBridgeSegmentDef(
            groupId = 0,
            rect = RectObj(140f, 0f, 660f, 20f)
        )
    ),
    coinPickups = listOf(
        RectObj(420f, -42f, 22f, 22f),
    )
)
