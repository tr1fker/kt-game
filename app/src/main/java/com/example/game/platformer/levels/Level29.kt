package com.example.game.platformer.levels

import com.example.game.platformer.ChargingLaserDef
import com.example.game.platformer.Level
import com.example.game.platformer.MovingPlatformDef
import com.example.game.platformer.MovingPlatformMotion
import com.example.game.platformer.RectObj
import com.example.game.platformer.WindZone

fun level29(): Level = Level(
    startX = 30f,
    startY = -60f,
    platforms = listOf(
        RectObj(0f, 0f, 200f, 20f),
    ),
    movingPlatforms = listOf(
        MovingPlatformDef(300f, 0f, 100f, 20f, MovingPlatformMotion.Horizontal, 300f, 3f),
    ),
    windZones = listOf(
        WindZone(x = 700f, y = -300f, width = 400f, height = 400f, forceX = 0f, forceY = -3000f)
    ),
    slipperyPlatforms = listOf(
        RectObj(750f, -400f, 300f, 20f),
    ),
    chargingLasers = listOf(
        ChargingLaserDef(
            emitterX = 1200f,
            emitterY = -400f,
            activationRadiusPx = 500f,
            chargeSeconds = 1.5f
        )
    ),
    hazards = listOf(
        RectObj(200f, 100f, 2000f, 20f),
        RectObj(1100f, -300f, 50f, 20f),
    ),
    checkpoints = listOf(
        RectObj(800f, -440f, 40f, 40f)
    ),
    goal = RectObj(1400f, -470f, 40f, 70f)
)