package com.example.game.platformer.levels

import com.example.game.platformer.KeyPickup
import com.example.game.platformer.Level
import com.example.game.platformer.LockedDoor
import com.example.game.platformer.RectObj

fun level23(): Level = Level(
    startX = 30f,
    startY = -60f,
    platforms = listOf(
        RectObj(0f, 0f, 1200f, 20f),
        RectObj(1000f, -80f, 550f, 20f),
        RectObj(0f, -160f, 1200f, 20f),
        RectObj(-350f, -240f, 550f, 20f),
        RectObj(0f, -320f, 1200f, 20f),
    ),
    hazards = listOf(
        RectObj(400f, -10f, 150f, 10f),
        RectObj(400f, -170f, 150f, 10f),
    ),
    checkpoints = listOf(
        RectObj(600f, -40f, 40f, 40f),
        RectObj(600f, -200f, 40f, 40f)
    ),
    keyPickups = listOf(
        KeyPickup(RectObj(1100f, -10f, 30f, 30f), doorId = 2),
        KeyPickup(RectObj(50f, -130f, 30f, 30f), doorId = 1),
    ),
    lockedDoors = listOf(
        LockedDoor(id = 1, rect = RectObj(300f, -160f, 20f, 160f)),
        LockedDoor(id = 2, rect = RectObj(900f, -320f, 20f, 160f)),
    ),
    goal = RectObj(1100f, -390f, 40f, 70f)
)