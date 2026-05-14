package com.example.game.platformer.levels

import com.example.game.platformer.CyclingHazardDef
import com.example.game.platformer.Level
import com.example.game.platformer.RectObj

/** Пол y=0: чекпоинт с y=0 (респавн на пол). Мигающие столбы опущены ближе к игроку, низ столба ~ -42 (выше головы -40). */
fun level52(): Level = Level(
    startX = 30f,
    startY = -60f,
    platforms = listOf(
        RectObj(0f, 0f, 1000f, 20f),
    ),
    hazards = emptyList(),
    checkpoints = listOf(
        RectObj(480f, 0f, 40f, 70f)
    ),
    goal = RectObj(920f, -70f, 40f, 70f),
    cyclingHazards = listOf(
        CyclingHazardDef(
            x = 300f,
            y = -240f,
            width = 26f,
            height = 198f,
            visibleSeconds = 1.3f,
            hiddenSeconds = 1.1f,
            phaseOffsetSeconds = 0f,
            fadeInSeconds = 0.11f,
            fadeOutSeconds = 0.13f
        ),
        CyclingHazardDef(
            x = 480f,
            y = -240f,
            width = 26f,
            height = 198f,
            visibleSeconds = 1.3f,
            hiddenSeconds = 1.1f,
            phaseOffsetSeconds = 0.45f,
            fadeInSeconds = 0.11f,
            fadeOutSeconds = 0.13f
        ),
        CyclingHazardDef(
            x = 660f,
            y = -240f,
            width = 26f,
            height = 198f,
            visibleSeconds = 1.3f,
            hiddenSeconds = 1.1f,
            phaseOffsetSeconds = 0.9f,
            fadeInSeconds = 0.11f,
            fadeOutSeconds = 0.13f
        ),
    ),
    coinPickups = listOf(
        RectObj(200f, -42f, 22f, 22f),
        RectObj(760f, -42f, 22f, 22f),
    )
)
