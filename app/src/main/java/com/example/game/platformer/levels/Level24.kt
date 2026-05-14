package com.example.game.platformer.levels

import androidx.compose.ui.graphics.Color
import com.example.game.platformer.ChargingLaserDef
import com.example.game.platformer.Level
import com.example.game.platformer.RectObj

fun level24(): Level = Level(
    startX = 30f,
    startY = -60f,
    platforms = listOf(
        RectObj(0f, 0f, 200f, 20f),
        RectObj(250f, -100f, 150f, 20f),
        RectObj(450f, -200f, 150f, 20f),
        RectObj(650f, -300f, 150f, 20f),
        RectObj(850f, -400f, 400f, 20f),
    ),
    hazards = emptyList(),
    checkpoints = listOf(
        RectObj(500f, -240f, 40f, 40f)
    ),
    goal = RectObj(1150f, -470f, 40f, 70f),
    chargingLasers = listOf(
        ChargingLaserDef(
            emitterX = 300f,
            emitterY = -400f,
            activationRadiusPx = 400f,
            chargeSeconds = 2.0f
        ),
        ChargingLaserDef(
            emitterX = 600f,
            emitterY = -500f,
            activationRadiusPx = 400f,
            chargeSeconds = 2.5f
        ),
        ChargingLaserDef(
            emitterX = 900f,
            emitterY = -600f,
            activationRadiusPx = 400f,
            chargeSeconds = 3.0f
        )
    )
)