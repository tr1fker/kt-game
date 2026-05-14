package com.example.game.platformer.levels

import com.example.game.platformer.DestructibleBlockDef
import com.example.game.platformer.Level
import com.example.game.platformer.RectObj


fun level14(): Level = Level(
    startX = 30f,
    startY = -60f,
    platforms = listOf(
        RectObj(0f, 0f, 100f, 20f),
        RectObj(0f, -100f, 20f, 100f),
        RectObj(80f, -100f, 20f, 100f),
        RectObj(100f, -100f, 100f, 20f),
        RectObj(180f, 0f, 100f, 20f),
        RectObj(180f, -100f, 20f, 100f),
        RectObj(260f, -100f, 20f, 100f),
        RectObj(280f, -100f, 400f, 20f),
    ),
    hazards = listOf(
        RectObj(350f, -110f, 100f, 10f),
    ),
    checkpoints = emptyList(),
    goal = RectObj(550f, -170f, 40f, 70f),
    destructibleBlocks = listOf(
        DestructibleBlockDef(RectObj(20f, -100f, 60f, 20f), hitsTotal = 3),
        DestructibleBlockDef(RectObj(200f, -100f, 60f, 20f), hitsTotal = 3),
    ),
    coinPickups = listOf(
        RectObj(219f, -32f, 22f, 22f),
        RectObj(219f, -64f, 22f, 22f),
    )
)
