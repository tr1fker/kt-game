package com.example.game.platformer

import kotlin.math.max
import kotlin.math.min


fun computeLevelContentBounds(level: Level, worldWidth: Float, worldHeight: Float): RectObj {
    var minX = worldWidth
    var minY = worldHeight
    var maxX = 0f
    var maxY = 0f

    fun expandTo(x1: Float, y1: Float, x2: Float, y2: Float) {
        minX = min(minX, x1)
        minY = min(minY, y1)
        maxX = max(maxX, x2)
        maxY = max(maxY, y2)
    }

    fun expandRect(r: RectObj) = expandTo(r.x, r.y, r.x + r.width, r.y + r.height)

    expandTo(level.startX, level.startY, level.startX + 40f, level.startY + 40f)
    level.platforms.forEach(::expandRect)
    level.hazards.forEach(::expandRect)
    level.cyclingHazards.forEach { c ->
        expandRect(RectObj(c.x, c.y, c.width, c.height))
    }
    level.checkpoints.forEach(::expandRect)
    expandRect(level.goal)
    level.windZones.forEach { z -> expandRect(RectObj(z.x, z.y, z.width, z.height)) }
    level.teleports.forEach { p ->
        expandRect(p.from)
        expandRect(p.to)
    }
    level.springPads.forEach { expandRect(it.rect) }
    level.keyPickups.forEach { expandRect(it.rect) }
    level.lockedDoors.forEach { expandRect(it.rect) }
    level.destructibleBlocks.forEach { expandRect(it.rect) }
    level.oneShotPlatforms.forEach { expandRect(it.rect) }
    level.pressurePlates.forEach { expandRect(it.rect) }
    level.timedBridgeSegments.forEach { expandRect(it.rect) }
    level.coinPickups.forEach(::expandRect)
    level.chargingLasers.forEach { l ->
        val mr = l.emitterMarkerRadiusWorld
        expandTo(l.emitterX - mr, l.emitterY - mr, l.emitterX + mr, l.emitterY + mr)
        val r = l.activationRadiusPx
        expandTo(l.emitterX - r, l.emitterY - r, l.emitterX + r, l.emitterY + r)
    }
    level.cyclingPlatforms.forEach { c ->
        expandRect(RectObj(c.x, c.y, c.width, c.height))
    }
    level.movingPlatforms.forEach { def ->
        when (def.motion) {
            MovingPlatformMotion.Horizontal -> {
                val halfW = def.width / 2f
                val halfH = def.height / 2f
                expandTo(
                    def.anchorX - halfW - def.range,
                    def.anchorY - halfH,
                    def.anchorX + halfW + def.range,
                    def.anchorY + halfH
                )
            }
            MovingPlatformMotion.Vertical -> {
                val halfW = def.width / 2f
                val halfH = def.height / 2f
                expandTo(
                    def.anchorX - halfW,
                    def.anchorY - halfH - def.range,
                    def.anchorX + halfW,
                    def.anchorY + halfH + def.range
                )
            }
            MovingPlatformMotion.Circular -> {
                val r = def.range + max(def.width, def.height) / 2f
                expandTo(def.anchorX - r, def.anchorY - r, def.anchorX + r, def.anchorY + r)
            }
        }
    }

    val pad = 36f
    minX = (minX - pad).coerceAtLeast(0f)
    minY = (minY - pad).coerceAtLeast(0f)
    maxX = (maxX + pad).coerceAtMost(worldWidth)
    maxY = (maxY + pad).coerceAtMost(worldHeight)
    val w = (maxX - minX).coerceAtLeast(120f)
    val h = (maxY - minY).coerceAtLeast(80f)
    return RectObj(minX, minY, w, h)
}

fun smoothCameraBlend(t: Float): Float {
    val x = t.coerceIn(0f, 1f)
    return x * x * (3f - 2f * x)
}
