package com.example.game.platformer.levels

import com.example.game.platformer.KeyPickup
import com.example.game.platformer.Level
import com.example.game.platformer.LockedDoor
import com.example.game.platformer.RectObj

/** Ключ слева, дверь в узком проходе, длинный коридор и чекпоинт на середине. */
fun level37(): Level = Level(
    startX = 30f,
    startY = -60f,
    platforms = listOf(
        RectObj(0f, 0f, 130f, 20f),
        RectObj(152f, 0f, 848f, 20f),
    ),
    hazards = listOf(
        RectObj(380f, -12f, 100f, 10f),
        RectObj(620f, -12f, 100f, 10f),
    ),
    checkpoints = listOf(
        RectObj(500f, 0f, 40f, 70f)
    ),
    goal = RectObj(920f, -70f, 40f, 70f),
    keyPickups = listOf(
        KeyPickup(rect = RectObj(48f, -48f, 28f, 28f), doorId = 1)
    ),
    lockedDoors = listOf(
        LockedDoor(id = 1, rect = RectObj(120f, -88f, 22f, 88f))
    ),
    coinPickups = listOf(
        RectObj(260f, -42f, 22f, 22f),
        RectObj(740f, -42f, 22f, 22f),
    )
)
