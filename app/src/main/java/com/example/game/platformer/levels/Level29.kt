package com.example.game.platformer.levels

import com.example.game.platformer.ChargingLaserDef
import com.example.game.platformer.Level
import com.example.game.platformer.MovingPlatformDef
import com.example.game.platformer.MovingPlatformMotion
import com.example.game.platformer.RectObj
import com.example.game.platformer.WindZone

fun level29(): Level {
    val startPlatform = RectObj(0f, 0f, 250f, 20f)
    val interPlatform = RectObj(1050f, -150f, 200f, 20f)
    val midPlatform = RectObj(1200f, -300f, 400f, 20f)
    val slipperyBridge = RectObj(1600f, -400f, 350f, 20f)
    val finalPlatform = RectObj(2000f, -500f, 400f, 20f)

    return Level(
        startX = 30f,
        startY = -60f,
        platforms = listOf(startPlatform, interPlatform, midPlatform, slipperyBridge, finalPlatform),
        movingPlatforms = listOf(
            MovingPlatformDef(450f, 0f, 150f, 20f, MovingPlatformMotion.Horizontal, 150f, 4f),
            MovingPlatformDef(850f, -100f, 150f, 20f, MovingPlatformMotion.Vertical, 100f, 5f),
        ),
        windZones = listOf(
            WindZone(x = 1100f, y = -600f, width = 600f, height = 500f, forceX = 0f, forceY = -1200f)
        ),
        slipperyPlatforms = listOf(slipperyBridge),
        chargingLasers = listOf(
            ChargingLaserDef(
                emitterX = 1800f,
                emitterY = -700f,
                activationRadiusPx = 500f,
                chargeSeconds = 4.0f
            ),
            ChargingLaserDef(
                emitterX = 2200f,
                emitterY = -850f,
                activationRadiusPx = 500f,
                chargeSeconds = 4.5f
            )
        ),
        hazards = listOf(
            RectObj(-200f, 150f, 3500f, 40f),
        ),
        checkpoints = listOf(
            RectObj(1300f, -340f, 40f, 40f),
            RectObj(1750f, -440f, 40f, 40f)
        ),
        goal = RectObj(2300f, -570f, 40f, 70f)
    )
}
