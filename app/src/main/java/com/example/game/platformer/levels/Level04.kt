package com.example.game.platformer.levels

import androidx.compose.ui.graphics.Color
import com.example.game.platformer.Level
import com.example.game.platformer.MovingPlatformDef
import com.example.game.platformer.MovingPlatformMotion
import com.example.game.platformer.RectObj
import kotlin.collections.listOf


fun level04(): Level = Level(
    startX = 30f,
    startY = -60f,
    platforms = listOf(
        RectObj(0f, 0f, 100f, 20f),
        RectObj(0f, -300f, 100f, 20f),
        RectObj(150f, -400f, 100f, 20f),
        RectObj(400f, -400f, 100f, 20f),
        RectObj(600f, -300f, 100f, 20f),
    ),
    hazards = listOf(
        RectObj(300f, -430f, 50f, 20f)
    ),
    checkpoints = emptyList(),
    goal = RectObj(635f, -370f, 30f, 68f),
    movingPlatforms = listOf(
        MovingPlatformDef(
            anchorX = 50f,
            anchorY = -100f,
            width = 100f,
            height = 20f,
            motion = MovingPlatformMotion.Horizontal,
            range = 200f,
            periodSeconds = 7f,
            phase01 = 0.25f,
            color = Color(0xFF9B59B6)
        ),
        MovingPlatformDef(
            anchorX = 50f,
            anchorY = -200f,
            width = 100f,
            height = 20f,
            motion = MovingPlatformMotion.Horizontal,
            range = 200f,
            periodSeconds = 7f,
            phase01 = 0.75f,
            color = Color(0xFF9B59B6)
        ),
    ),
    coinPickups = listOf(
        RectObj(39f, -500f, 22f, 22f),
    )
)
