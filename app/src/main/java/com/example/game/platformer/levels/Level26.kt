package com.example.game.platformer.levels

import com.example.game.platformer.Level
import com.example.game.platformer.MovingPlatformDef
import com.example.game.platformer.MovingPlatformMotion
import com.example.game.platformer.RectObj

fun level26(): Level = Level(
    startX = 30f,
    startY = -60f,
    platforms = listOf(
        RectObj(0f, 0f, 200f, 20f),
        RectObj(1200f, -300f, 200f, 20f),
    ),
    movingPlatforms = listOf(
        MovingPlatformDef(300f, 0f, 100f, 20f, MovingPlatformMotion.Horizontal, 200f, 3f),
        MovingPlatformDef(600f, -100f, 100f, 20f, MovingPlatformMotion.Vertical, 200f, 4f),
        MovingPlatformDef(900f, -200f, 100f, 20f, MovingPlatformMotion.Circular, 100f, 5f),
    ),
    hazards = listOf(
        RectObj(200f, 100f, 1000f, 20f)
    ),
    checkpoints = listOf(
        RectObj(600f, -250f, 40f, 40f)
    ),
    goal = RectObj(1300f, -370f, 40f, 70f)
)