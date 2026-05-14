package com.example.game.platformer.levels

import androidx.compose.ui.graphics.Color
import com.example.game.platformer.Level
import com.example.game.platformer.MovingPlatformDef
import com.example.game.platformer.MovingPlatformMotion
import com.example.game.platformer.RectObj
import com.example.game.platformer.TeleportPair


fun level11(): Level = Level(
    startX = 30f,
    startY = -60f,
    platforms = listOf(
        RectObj(0f, 0f, 200f, 20f),
        RectObj(180f, -150f, 20f, 150f),
        RectObj(0f, -170f, 200f, 20f),
        RectObj(180f, 20f, 20f, 150f),
        RectObj(0f, 170f, 200f, 20f),
        RectObj(0f, 20f, 20f, 150f),
    ),
    hazards = listOf(
    ),
    checkpoints = emptyList(),
    goal = RectObj(40f, 100f, 30f, 70f),
    teleports = listOf(
        TeleportPair(
            from = RectObj(130f, -70f, 30f, 70f),
            to = RectObj(20f, -240f, 30f, 70f)
        ),
        TeleportPair(
            from = RectObj(600f, -170f, 70f, 30f),
            to = RectObj(130f, 100f, 30f, 70f)
        )
    ),
    movingPlatforms = listOf(
        MovingPlatformDef(
            anchorX = 350f,
            anchorY = -165f,
            width = 100f,
            height = 10f,
            motion = MovingPlatformMotion.Horizontal,
            range = 100f,
            periodSeconds = 5.2f,
            phase01 = 0f,
            color = Color(0xFF1ABC9C)
        ),
    ),
    coinPickups = listOf(
        RectObj(-82f, -121f, 22f, 22f),
    )
)
