package com.example.game.platformer.levels

import androidx.compose.ui.graphics.Color
import com.example.game.platformer.Level
import com.example.game.platformer.MovingPlatformDef
import com.example.game.platformer.MovingPlatformMotion
import com.example.game.platformer.RectObj


fun level05(): Level = Level(
    startX = 30f,
    startY = -60f,
    platforms = listOf(
        RectObj(0f, 0f, 100f, 20f),
        RectObj(-50f, 100f, 200f, 20f),
        RectObj(0f, -350f, 100f, 20f),
        RectObj(0f, -450f, 100f, 20f),
        RectObj(0f, -550f, 100f, 20f),
        RectObj(0f, -650f, 100f, 20f),
    ),
    hazards = listOf(
        RectObj(10f, -660f, 80f, 10f),
    ),
    checkpoints = emptyList(),
    goal = RectObj(35f, -520f, 30f, 70f),
    movingPlatforms = listOf(
        MovingPlatformDef(
            anchorX = -100f,
            anchorY = -150f,
            width = 100f,
            height = 20f,
            motion = MovingPlatformMotion.Vertical,
            range = 150f,
            periodSeconds = 7f,
            phase01 = 0f,
            color = Color(0xFFE67E22)
        ),
        MovingPlatformDef(
            anchorX = 200f,
            anchorY = -150f,
            width = 100f,
            height = 20f,
            motion = MovingPlatformMotion.Vertical,
            range = 150f,
            periodSeconds = 7f,
            phase01 = 0f,
            color = Color(0xFFE67E22)
        ),
    ),
    coinPickups = listOf(
        RectObj(39f, 58f, 22f, 22f),
        RectObj(39f, -592f, 22f, 22f),
    )
)
