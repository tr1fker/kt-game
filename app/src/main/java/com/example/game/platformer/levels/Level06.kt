package com.example.game.platformer.levels

import androidx.compose.ui.graphics.Color
import com.example.game.platformer.Level
import com.example.game.platformer.MovingPlatformDef
import com.example.game.platformer.MovingPlatformMotion
import com.example.game.platformer.RectObj


fun level06(): Level = Level(
    startX = 20f,
    startY = -60f,
    platforms = listOf(
        RectObj(0f, 0f, 200f, 20f),
        RectObj(1400f, 0f, 300f, 20f),
    ),
    hazards = listOf(

    ),
    checkpoints = emptyList(),
    goal = RectObj(1650f, -70f, 30f, 70f),
    movingPlatforms = listOf(
        MovingPlatformDef(
            anchorX = 350f,
            anchorY = 0f,
            width = 100f,
            height = 10f,
            motion = MovingPlatformMotion.Circular,
            range = 100f,
            periodSeconds = 5.2f,
            phase01 = 0f,
            color = Color(0xFF1ABC9C)
        ),
        MovingPlatformDef(
            anchorX = 650f,
            anchorY = 0f,
            width = 100f,
            height = 10f,
            motion = MovingPlatformMotion.Circular,
            range = 100f,
            periodSeconds = 5.2f,
            phase01 = 0.5f,
            color = Color(0xFF1ABC9C)
        ),
        MovingPlatformDef(
            anchorX = 950f,
            anchorY = 0f,
            width = 100f,
            height = 10f,
            motion = MovingPlatformMotion.Circular,
            range = 100f,
            periodSeconds = 5.2f,
            phase01 = 0f,
            color = Color(0xFF1ABC9C)
        ),
        MovingPlatformDef(
            anchorX = 1250f,
            anchorY = 0f,
            width = 100f,
            height = 10f,
            motion = MovingPlatformMotion.Circular,
            range = 100f,
            periodSeconds = 5.2f,
            phase01 = 0.5f,
            color = Color(0xFF1ABC9C)
        ),
    ),
    coinPickups = listOf(
        RectObj(339f, -11f, 22f, 22f),
        RectObj(639f, -11f, 22f, 22f),
        RectObj(939f, -11f, 22f, 22f),
        RectObj(1239f, -11f, 22f, 22f),
    )
)
