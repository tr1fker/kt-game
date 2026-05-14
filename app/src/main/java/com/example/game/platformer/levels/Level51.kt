package com.example.game.platformer.levels

import com.example.game.platformer.KeyPickup
import com.example.game.platformer.Level
import com.example.game.platformer.LockedDoor
import com.example.game.platformer.RectObj

/** Компактный ключ + дверь и короткий коридор с двумя шипами на полу. */
fun level51(): Level = Level(
    startX = 30f,
    startY = -60f,
    platforms = listOf(
        RectObj(0f, 0f, 120f, 20f),
        RectObj(142f, 0f, 858f, 20f),
    ),
    hazards = listOf(
        RectObj(360f, -12f, 90f, 10f),
        RectObj(560f, -12f, 90f, 10f),
    ),
    checkpoints = emptyList(),
    goal = RectObj(920f, -70f, 40f, 70f),
    keyPickups = listOf(
        KeyPickup(rect = RectObj(45f, -48f, 28f, 28f), doorId = 1)
    ),
    lockedDoors = listOf(
        LockedDoor(id = 1, rect = RectObj(110f, -88f, 22f, 88f))
    ),
    coinPickups = listOf(
        RectObj(720f, -42f, 22f, 22f),
    )
)
