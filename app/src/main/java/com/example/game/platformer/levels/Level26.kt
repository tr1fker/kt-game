package com.example.game.platformer.levels

import com.example.game.platformer.Level
import com.example.game.platformer.MovingPlatformDef
import com.example.game.platformer.MovingPlatformMotion
import com.example.game.platformer.RectObj

fun level26(): Level = Level(
    startX = 30f,
    startY = -60f,
    platforms = listOf(
        RectObj(0f, 0f, 250f, 20f),
        RectObj(1300f, -300f, 300f, 20f),
    ),
    movingPlatforms = listOf(
        MovingPlatformDef(400f, 0f, 150f, 20f, MovingPlatformMotion.Horizontal, 150f, 4f),
        MovingPlatformDef(750f, -100f, 150f, 20f, MovingPlatformMotion.Vertical, 150f, 5f),
        MovingPlatformDef(1100f, -200f, 150f, 20f, MovingPlatformMotion.Circular, 80f, 6f),
    ),
    hazards = listOf(
        RectObj(-200f, 150f, 2000f, 40f)
    ),
    checkpoints = listOf(
        RectObj(750f, -250f, 40f, 40f)
    ),
    goal = RectObj(1500f, -370f, 40f, 70f)
)
