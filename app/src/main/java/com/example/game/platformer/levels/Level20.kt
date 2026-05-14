package com.example.game.platformer.levels

import androidx.compose.ui.graphics.Color
import com.example.game.platformer.ChargingLaserDef
import com.example.game.platformer.Level
import com.example.game.platformer.RectObj


fun level20(): Level = Level(
    startX = 30f,
    startY = -60f,
    platforms = listOf(
        RectObj(0f, 0f, 300f, 20f),
        RectObj(300f, -100f, 50f, 20f),
        RectObj(350f, -200f, 50f, 20f),
        RectObj(400f, -300f, 50f, 20f),
        RectObj(450f, -400f, 400f, 20f),
    ),
    hazards = emptyList(),
    checkpoints = emptyList(),
    goal = RectObj(790f, -470f, 40f, 70f),
    chargingLasers = listOf(
        ChargingLaserDef(
            emitterX = 300f,
            emitterY = -300f,
            activationRadiusPx = 300f,
            chargeSeconds = 9f,
            emitterMarkerRadiusWorld = 20f,
            coreColor = Color(0xFFFF1744),
            warnGlowColor = Color(0xFF4FC3F7)
        )
    )
)
