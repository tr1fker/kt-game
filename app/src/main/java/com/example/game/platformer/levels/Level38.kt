package com.example.game.platformer.levels

import com.example.game.platformer.Level
import com.example.game.platformer.RectObj
import com.example.game.platformer.SpringPad

/**
 * Ступени с пружинами: умеренные импульсы, шипы только под «ямы» между полками,
 * без пересечения дуги прыжка по центру зазора.
 */
fun level38(): Level = Level(
    startX = 30f,
    startY = -60f,
    platforms = listOf(
        RectObj(0f, 0f, 160f, 20f),
        RectObj(240f, -90f, 130f, 20f),
        RectObj(480f, -180f, 130f, 20f),
        RectObj(720f, -260f, 280f, 20f),
    ),
    hazards = listOf(
        RectObj(165f, -12f, 65f, 10f),
        RectObj(405f, -12f, 65f, 10f),
    ),
    checkpoints = emptyList(),
    goal = RectObj(920f, -330f, 40f, 70f),
    springPads = listOf(
        SpringPad(RectObj(70f, -12f, 68f, 18f), jumpImpulse = -640f),
        SpringPad(RectObj(300f, -102f, 68f, 18f), jumpImpulse = -660f),
        SpringPad(RectObj(540f, -192f, 68f, 18f), jumpImpulse = -680f),
    ),
    coinPickups = listOf(
        RectObj(119f, -42f, 22f, 22f),
        RectObj(539f, -222f, 22f, 22f),
        RectObj(820f, -302f, 22f, 22f),
    )
)
