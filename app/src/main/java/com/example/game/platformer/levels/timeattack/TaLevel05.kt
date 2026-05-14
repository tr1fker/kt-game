package com.example.game.platformer.levels.timeattack

import com.example.game.platformer.CyclingPlatformDef
import com.example.game.platformer.Level
import com.example.game.platformer.OneShotPlatformDef
import com.example.game.platformer.RectObj


fun taLevel05(): Level = Level(
    startX = 20f,
    startY = -60f,
    platforms = listOf(
        RectObj(0f, 0f, 100f, 20f),
        RectObj(0f, -700f, 300f, 20f),
    ),
    hazards = emptyList(),
    checkpoints = emptyList(),
    goal = RectObj(240f, -770f, 40f, 70f),
    oneShotPlatforms = listOf(
        OneShotPlatformDef(RectObj(150f, -100f, 100f, 20f), crumbleDelayMs = 1000L, respawnDelayMs = 2000L),
        OneShotPlatformDef(RectObj(-150f, -200f, 100f, 20f), crumbleDelayMs = 1000L, respawnDelayMs = 2000L),
        OneShotPlatformDef(RectObj(150f, -300f, 100f, 20f), crumbleDelayMs = 1000L, respawnDelayMs = 2000L),
        OneShotPlatformDef(RectObj(-150f, -400f, 100f, 20f), crumbleDelayMs = 1000L, respawnDelayMs = 2000L),
        OneShotPlatformDef(RectObj(150f, -500f, 100f, 20f), crumbleDelayMs = 1000L, respawnDelayMs = 2000L),
        OneShotPlatformDef(RectObj(-150f, -600f, 100f, 20f), crumbleDelayMs = 1000L, respawnDelayMs = 2000L),
    ),
    timeLimitSeconds = 20
)
