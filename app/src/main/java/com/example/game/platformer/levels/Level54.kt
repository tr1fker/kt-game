package com.example.game.platformer.levels

import com.example.game.platformer.DestructibleBlockDef
import com.example.game.platformer.Level
import com.example.game.platformer.RectObj

/** Четыре разрушаемых блока подряд над ямой, затем длинная полка. */
fun level54(): Level = Level(
    startX = 30f,
    startY = -60f,
    platforms = listOf(
        RectObj(0f, 0f, 110f, 20f),
        RectObj(450f, -45f, 550f, 20f),
    ),
    hazards = listOf(
        RectObj(110f, -12f, 340f, 10f),
    ),
    checkpoints = listOf(
        RectObj(520f, -45f, 40f, 70f)
    ),
    goal = RectObj(900f, -115f, 40f, 70f),
    destructibleBlocks = listOf(
        DestructibleBlockDef(RectObj(130f, -45f, 72f, 20f), hitsTotal = 3),
        DestructibleBlockDef(RectObj(212f, -45f, 72f, 20f), hitsTotal = 3),
        DestructibleBlockDef(RectObj(294f, -45f, 72f, 20f), hitsTotal = 3),
        DestructibleBlockDef(RectObj(376f, -45f, 72f, 20f), hitsTotal = 3),
    ),
    coinPickups = listOf(
        RectObj(245f, -87f, 22f, 22f),
        RectObj(700f, -87f, 22f, 22f),
    )
)
