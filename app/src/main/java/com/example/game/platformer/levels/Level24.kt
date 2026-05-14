package com.example.game.platformer.levels

import androidx.compose.ui.graphics.Color
import com.example.game.platformer.ChargingLaserDef
import com.example.game.platformer.Level
import com.example.game.platformer.RectObj

fun level24(): Level = Level(
    startX = 30f,
    startY = -60f,
    platforms = listOf(
        RectObj(0f, 0f, 300f, 20f),
        RectObj(350f, -100f, 250f, 20f),
        RectObj(650f, -200f, 250f, 20f),
        RectObj(950f, -300f, 250f, 20f),
        RectObj(1250f, -400f, 400f, 20f),
    ),
    hazards = emptyList(),
    checkpoints = listOf(
        RectObj(750f, -240f, 40f, 40f)
    ),
    goal = RectObj(1550f, -470f, 40f, 70f),
    chargingLasers = listOf(
        ChargingLaserDef(
            emitterX = 300f,
            emitterY = -400f,
            activationRadiusPx = 500f,
            chargeSeconds = 3.2f
        ),
        ChargingLaserDef(
            emitterX = 700f,
            emitterY = -500f,
            activationRadiusPx = 500f,
            chargeSeconds = 3.8f
        ),
        ChargingLaserDef(
            emitterX = 1100f,
            emitterY = -600f,
            activationRadiusPx = 500f,
            chargeSeconds = 4.2f
        )
    )
)
