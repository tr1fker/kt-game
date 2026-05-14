package com.example.game.platformer.levels

import androidx.compose.ui.graphics.Color
import com.example.game.platformer.Level
import com.example.game.platformer.MovingPlatformDef
import com.example.game.platformer.MovingPlatformMotion
import com.example.game.platformer.RectObj

/** Лифт на вертикальной подвижной платформе с нижнего пола на верхнюю полку. */
fun level31(): Level = Level(
    startX = 30f,
    startY = -60f,
    platforms = listOf(
        RectObj(0f, 0f, 160f, 20f),
        RectObj(120f, -260f, 880f, 20f),
    ),
    hazards = emptyList(),
    checkpoints = listOf(
        RectObj(480f, -260f, 40f, 70f)
    ),
    goal = RectObj(900f, -330f, 40f, 70f),
    movingPlatforms = listOf(
        MovingPlatformDef(
            anchorX = 200f,
            anchorY = -130f,
            width = 110f,
            height = 16f,
            motion = MovingPlatformMotion.Vertical,
            range = 115f,
            periodSeconds = 5.4f,
            phase01 = 0f,
            color = Color(0xFF5C6BC0)
        )
    ),
    coinPickups = listOf(
        RectObj(245f, -152f, 22f, 22f),
        RectObj(640f, -302f, 22f, 22f),
    )
)
