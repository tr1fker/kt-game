package com.example.game.platformer.levels

import com.example.game.platformer.KeyPickup
import com.example.game.platformer.Level
import com.example.game.platformer.LockedDoor
import com.example.game.platformer.RectObj
import com.example.game.platformer.TeleportPair


fun level13(): Level = Level(
    startX = 20f,
    startY = -60f,
    platforms = listOf(
        RectObj(-200f, 0f, 600f, 20f),
        RectObj(-200f, -100f, 20f, 100f),
        RectObj(-180f, -100f, 100f, 20f),
        RectObj(380f, -100f, 20f, 100f),
        RectObj(0f, -120f, 600f, 20f),
        RectObj(0f, -300f, 20f, 180f),
    ),
    hazards = listOf(
        RectObj(170f, -130f, 130f, 10f),
    ),
    checkpoints = emptyList(),
    goal = RectObj(500f, -190f, 40f, 70f),
    keyPickups = listOf(
        KeyPickup(rect = RectObj(330f, -50f, 30f, 30f), doorId = 1)
    ),
    lockedDoors = listOf(
        LockedDoor(id = 1, rect = RectObj(-100f, -80f, 20f, 80f))
    ),
    teleports = listOf(
        TeleportPair(
            from = RectObj(-160f, -70f, 30f, 70f),
            to = RectObj(40f, -190f, 30f, 70f)
        )
    ),
    coinPickups = listOf(
        RectObj(-175f, -142f, 22f, 22f),
        RectObj(-128f, -142f, 22f, 22f),
        RectObj(560f, -162f, 22f, 22f),
    )
)
