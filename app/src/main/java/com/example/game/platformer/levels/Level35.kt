package com.example.game.platformer.levels

import com.example.game.platformer.Level
import com.example.game.platformer.OneShotPlatformDef
import com.example.game.platformer.RectObj

/** Лесенка из одноразовых платформ с запасом по таймерам и финальной опорой. */
fun level35(): Level = Level(
    startX = 30f,
    startY = -60f,
    platforms = listOf(
        RectObj(0f, 0f, 150f, 20f),
        RectObj(700f, -400f, 300f, 20f),
    ),
    hazards = listOf(
        RectObj(160f, -12f, 120f, 10f),
    ),
    checkpoints = listOf(
        RectObj(310f, -180f, 40f, 70f)
    ),
    goal = RectObj(900f, -470f, 40f, 70f),
    oneShotPlatforms = listOf(
        OneShotPlatformDef(RectObj(150f, -100f, 100f, 18f), crumbleDelayMs = 560L, respawnDelayMs = 2200L),
        OneShotPlatformDef(RectObj(280f, -180f, 100f, 18f), crumbleDelayMs = 560L, respawnDelayMs = 2200L),
        OneShotPlatformDef(RectObj(410f, -260f, 100f, 18f), crumbleDelayMs = 560L, respawnDelayMs = 2200L),
        OneShotPlatformDef(RectObj(540f, -330f, 110f, 18f), crumbleDelayMs = 600L, respawnDelayMs = 2400L),
    ),
    coinPickups = listOf(
        RectObj(314f, -222f, 22f, 22f),
        RectObj(585f, -372f, 22f, 22f),
    )
)
