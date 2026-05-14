package com.example.game.platformer.levels

import com.example.game.platformer.Level
import com.example.game.platformer.OneShotPlatformDef
import com.example.game.platformer.RectObj

/** Три одноразовые полки и чекпоинт на второй; финиш наверху справа. */
fun level55(): Level = Level(
    startX = 30f,
    startY = -60f,
    platforms = listOf(
        RectObj(0f, 0f, 140f, 20f),
        RectObj(555f, -262f, 130f, 20f),
        RectObj(680f, -340f, 320f, 20f),
    ),
    hazards = listOf(
        RectObj(150f, -12f, 100f, 10f),
    ),
    checkpoints = listOf(
        RectObj(290f, -180f, 40f, 70f)
    ),
    goal = RectObj(900f, -410f, 40f, 70f),
    oneShotPlatforms = listOf(
        OneShotPlatformDef(RectObj(160f, -100f, 100f, 18f), crumbleDelayMs = 540L, respawnDelayMs = 2100L),
        OneShotPlatformDef(RectObj(280f, -180f, 100f, 18f), crumbleDelayMs = 540L, respawnDelayMs = 2100L),
        OneShotPlatformDef(RectObj(420f, -260f, 110f, 18f), crumbleDelayMs = 580L, respawnDelayMs = 2300L),
    ),
    coinPickups = listOf(
        RectObj(314f, -222f, 22f, 22f),
        RectObj(780f, -382f, 22f, 22f),
    )
)
