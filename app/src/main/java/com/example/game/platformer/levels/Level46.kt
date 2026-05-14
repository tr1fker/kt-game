package com.example.game.platformer.levels

import com.example.game.platformer.DestructibleBlockDef
import com.example.game.platformer.Level
import com.example.game.platformer.RectObj

/** Три разрушаемых блока подряд и длинный выход; чекпоинт после «стены». */
fun level46(): Level = Level(
    startX = 30f,
    startY = -60f,
    platforms = listOf(
        RectObj(0f, 0f, 120f, 20f),
        RectObj(400f, -40f, 600f, 20f),
    ),
    hazards = listOf(
        RectObj(120f, -12f, 270f, 10f),
    ),
    checkpoints = listOf(
        RectObj(420f, -40f, 40f, 70f)
    ),
    goal = RectObj(900f, -110f, 40f, 70f),
    destructibleBlocks = listOf(
        DestructibleBlockDef(RectObj(150f, -40f, 75f, 20f), hitsTotal = 3),
        DestructibleBlockDef(RectObj(235f, -40f, 75f, 20f), hitsTotal = 3),
        DestructibleBlockDef(RectObj(320f, -40f, 75f, 20f), hitsTotal = 3),
    ),
    coinPickups = listOf(
        RectObj(262f, -82f, 22f, 22f),
        RectObj(720f, -82f, 22f, 22f),
    )
)
