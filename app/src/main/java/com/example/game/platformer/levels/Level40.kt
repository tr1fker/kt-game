package com.example.game.platformer.levels

import androidx.compose.ui.graphics.Color
import com.example.game.platformer.Level
import com.example.game.platformer.MovingPlatformDef
import com.example.game.platformer.MovingPlatformMotion
import com.example.game.platformer.RectObj

/** Три «парома» только по горизонтали над ямой. */
fun level40(): Level = Level(
    startX = 30f,
    startY = -60f,
    platforms = listOf(
        RectObj(0f, 0f, 140f, 20f),
        RectObj(830f, 0f, 170f, 20f),
    ),
    hazards = listOf(
        RectObj(140f, -12f, 690f, 10f),
    ),
    checkpoints = emptyList(),
    goal = RectObj(920f, -70f, 40f, 70f),
    movingPlatforms = listOf(
        MovingPlatformDef(
            anchorX = 260f,
            anchorY = -26f,
            width = 100f,
            height = 14f,
            motion = MovingPlatformMotion.Horizontal,
            range = 95f,
            periodSeconds = 4.5f,
            phase01 = 0f,
            color = Color(0xFF00897B)
        ),
        MovingPlatformDef(
            anchorX = 460f,
            anchorY = -26f,
            width = 100f,
            height = 14f,
            motion = MovingPlatformMotion.Horizontal,
            range = 95f,
            periodSeconds = 4.5f,
            phase01 = 0.33f,
            color = Color(0xFF00897B)
        ),
        MovingPlatformDef(
            anchorX = 660f,
            anchorY = -26f,
            width = 100f,
            height = 14f,
            motion = MovingPlatformMotion.Horizontal,
            range = 95f,
            periodSeconds = 4.5f,
            phase01 = 0.66f,
            color = Color(0xFF00897B)
        ),
    ),
    coinPickups = listOf(
        RectObj(408f, -42f, 22f, 22f),
        RectObj(708f, -42f, 22f, 22f),
    )
)
