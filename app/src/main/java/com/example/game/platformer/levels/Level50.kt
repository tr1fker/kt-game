package com.example.game.platformer.levels

import androidx.compose.ui.graphics.Color
import com.example.game.platformer.Level
import com.example.game.platformer.MovingPlatformDef
import com.example.game.platformer.MovingPlatformMotion
import com.example.game.platformer.RectObj

/** Четыре горизонтальных парома с разными фазами. */
fun level50(): Level = Level(
    startX = 30f,
    startY = -60f,
    platforms = listOf(
        RectObj(0f, 0f, 120f, 20f),
        RectObj(810f, 0f, 190f, 20f),
    ),
    hazards = listOf(
        RectObj(120f, -12f, 690f, 10f),
    ),
    checkpoints = emptyList(),
    goal = RectObj(920f, -70f, 40f, 70f),
    movingPlatforms = listOf(
        MovingPlatformDef(
            anchorX = 220f,
            anchorY = -24f,
            width = 92f,
            height = 14f,
            motion = MovingPlatformMotion.Horizontal,
            range = 88f,
            periodSeconds = 4.2f,
            phase01 = 0f,
            color = Color(0xFF6D4C41)
        ),
        MovingPlatformDef(
            anchorX = 380f,
            anchorY = -24f,
            width = 92f,
            height = 14f,
            motion = MovingPlatformMotion.Horizontal,
            range = 88f,
            periodSeconds = 4.2f,
            phase01 = 0.25f,
            color = Color(0xFF6D4C41)
        ),
        MovingPlatformDef(
            anchorX = 540f,
            anchorY = -24f,
            width = 92f,
            height = 14f,
            motion = MovingPlatformMotion.Horizontal,
            range = 88f,
            periodSeconds = 4.2f,
            phase01 = 0.5f,
            color = Color(0xFF6D4C41)
        ),
        MovingPlatformDef(
            anchorX = 700f,
            anchorY = -24f,
            width = 92f,
            height = 14f,
            motion = MovingPlatformMotion.Horizontal,
            range = 88f,
            periodSeconds = 4.2f,
            phase01 = 0.75f,
            color = Color(0xFF6D4C41)
        ),
    ),
    coinPickups = listOf(
        RectObj(368f, -42f, 22f, 22f),
        RectObj(688f, -42f, 22f, 22f),
    )
)
