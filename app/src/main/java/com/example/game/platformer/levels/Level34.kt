package com.example.game.platformer.levels

import com.example.game.platformer.DestructibleBlockDef
import com.example.game.platformer.Level
import com.example.game.platformer.RectObj

/** Два разрушаемых блока над ямой — дальше сплошная полка до финиша. */
fun level34(): Level = Level(
    startX = 30f,
    startY = -60f,
    platforms = listOf(
        RectObj(0f, 0f, 120f, 20f),
        RectObj(380f, -50f, 620f, 20f),
    ),
    hazards = listOf(
        RectObj(120f, -12f, 260f, 10f),
    ),
    checkpoints = emptyList(),
    goal = RectObj(900f, -120f, 40f, 70f),
    destructibleBlocks = listOf(
        DestructibleBlockDef(RectObj(150f, -50f, 95f, 20f), hitsTotal = 3),
        DestructibleBlockDef(RectObj(255f, -50f, 95f, 20f), hitsTotal = 3),
    ),
    coinPickups = listOf(
        RectObj(197f, -92f, 22f, 22f),
        RectObj(620f, -92f, 22f, 22f),
    )
)
