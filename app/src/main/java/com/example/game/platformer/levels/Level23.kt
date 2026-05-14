package com.example.game.platformer.levels

import com.example.game.platformer.KeyPickup
import com.example.game.platformer.Level
import com.example.game.platformer.LockedDoor
import com.example.game.platformer.RectObj

fun level23(): Level = Level(
    startX = 30f,
    startY = -60f,
    platforms = listOf(
        RectObj(0f, 0f, 200f, 20f),
        RectObj(300f, 0f, 200f, 20f),
        RectObj(600f, 0f, 200f, 20f),
        RectObj(900f, 0f, 200f, 20f),
        
        RectObj(0f, -200f, 200f, 20f),
        RectObj(300f, -200f, 200f, 20f),
        RectObj(600f, -200f, 200f, 20f),
        RectObj(900f, -200f, 200f, 20f),
    ),
    hazards = listOf(
        RectObj(200f, 50f, 100f, 20f),
        RectObj(500f, 50f, 100f, 20f),
        RectObj(800f, 50f, 100f, 20f),
    ),
    checkpoints = listOf(
        RectObj(450f, -40f, 40f, 40f)
    ),
    keyPickups = listOf(
        KeyPickup(RectObj(50f, -250f, 30f, 30f), doorId = 1),
        KeyPickup(RectObj(950f, -50f, 30f, 30f), doorId = 2),
    ),
    lockedDoors = listOf(
        LockedDoor(id = 1, rect = RectObj(250f, -200f, 20f, 200f)),
        LockedDoor(id = 2, rect = RectObj(850f, -200f, 20f, 200f)),
    ),
    goal = RectObj(1050f, -70f, 40f, 70f)
)