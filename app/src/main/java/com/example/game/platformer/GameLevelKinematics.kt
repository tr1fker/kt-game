package com.example.game.platformer

import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

private fun omega(periodSeconds: Float): Float =
    if (periodSeconds <= 0.05f) 2f * PI.toFloat() * 20f else 2f * PI.toFloat() / periodSeconds

private fun phaseRadians(def: MovingPlatformDef, t: Float): Float =
    omega(def.periodSeconds) * t + def.phase01 * 2f * PI.toFloat()

fun movingPlatformRect(def: MovingPlatformDef, timeSeconds: Float): RectObj {
    val (cx, cy) = movingPlatformCenter(def, timeSeconds)
    return RectObj(cx - def.width / 2f, cy - def.height / 2f, def.width, def.height)
}

fun movingPlatformCenter(def: MovingPlatformDef, timeSeconds: Float): Pair<Float, Float> {
    val th = phaseRadians(def, timeSeconds)
    return when (def.motion) {
        MovingPlatformMotion.Horizontal ->
            Pair(def.anchorX + def.range * sin(th), def.anchorY)
        MovingPlatformMotion.Vertical ->
            Pair(def.anchorX, def.anchorY + def.range * sin(th))
        MovingPlatformMotion.Circular ->
            Pair(def.anchorX + def.range * cos(th), def.anchorY + def.range * sin(th))
    }
}


fun movingPlatformVelocity(def: MovingPlatformDef, timeSeconds: Float): Pair<Float, Float> {
    val w = omega(def.periodSeconds)
    val th = phaseRadians(def, timeSeconds)
    return when (def.motion) {
        MovingPlatformMotion.Horizontal ->
            Pair(def.range * w * cos(th), 0f)
        MovingPlatformMotion.Vertical ->
            Pair(0f, def.range * w * cos(th))
        MovingPlatformMotion.Circular ->
            Pair(-def.range * w * sin(th), def.range * w * cos(th))
    }
}

private fun smoothstep01(tRaw: Float): Float {
    val t = tRaw.coerceIn(0f, 1f)
    return t * t * (3f - 2f * t)
}


fun cyclingPlatformPhaseU(def: CyclingPlatformDef, timeSeconds: Float): Float {
    val cycle = def.visibleSeconds + def.hiddenSeconds
    if (cycle <= 0.05f) return 0f
    var u = (timeSeconds + def.phaseOffsetSeconds) % cycle
    if (u < 0f) u += cycle
    return u
}

fun cyclingPlatformVisible(def: CyclingPlatformDef, timeSeconds: Float): Boolean =
    cyclingPlatformPhaseU(def, timeSeconds) < def.visibleSeconds


fun cyclingPlatformDrawAlpha(def: CyclingPlatformDef, timeSeconds: Float): Float {
    val u = cyclingPlatformPhaseU(def, timeSeconds)
    if (u >= def.visibleSeconds) return 0f

    val maxFade = def.visibleSeconds * 0.45f
    var fi = def.fadeInSeconds.coerceIn(0.02f, maxFade)
    var fo = def.fadeOutSeconds.coerceIn(0.02f, maxFade)
    val cap = def.visibleSeconds * 0.9f
    if (fi + fo > cap) {
        val s = cap / (fi + fo)
        fi *= s
        fo *= s
    }
    return when {
        u < fi -> smoothstep01(u / fi.coerceAtLeast(1e-4f))
        u > def.visibleSeconds - fo -> 1f - smoothstep01((u - (def.visibleSeconds - fo)) / fo.coerceAtLeast(1e-4f))
        else -> 1f
    }
}


fun cyclingPlatformCollidable(def: CyclingPlatformDef, timeSeconds: Float): Boolean =
    cyclingPlatformDrawAlpha(def, timeSeconds) >= 0.38f

fun rectApproxEqual(a: RectObj, b: RectObj, eps: Float = 1.5f): Boolean =
    kotlin.math.abs(a.x - b.x) < eps &&
        kotlin.math.abs(a.y - b.y) < eps &&
        kotlin.math.abs(a.width - b.width) < eps &&
        kotlin.math.abs(a.height - b.height) < eps

fun slipperyContains(slippery: List<RectObj>, platform: RectObj): Boolean =
    slippery.any { rectApproxEqual(it, platform) }

fun springImpulseForRect(pads: List<SpringPad>, rect: RectObj): Float =
    pads.firstOrNull { rectApproxEqual(it.rect, rect) }?.jumpImpulse ?: -620f

fun pointInRect(px: Float, py: Float, r: RectObj): Boolean =
    px >= r.x && px < r.x + r.width && py >= r.y && py < r.y + r.height

fun windZoneToRect(zone: WindZone): RectObj =
    RectObj(zone.x, zone.y, zone.width, zone.height)

fun collectSolidEntries(
    level: Level,
    timeSeconds: Float,
    unlockedDoorIds: Set<Int> = emptySet(),
    destructibleHits: List<Int>? = null,
    elapsedMs: Long = 0L,
    oneShotCrumbleStartMs: List<Long>? = null,
    bridgeGroupActiveUntilMs: LongArray? = null
): List<SolidEntry> = buildList {
    level.platforms.forEach { r ->
        val kind = if (slipperyContains(level.slipperyPlatforms, r)) SolidKind.Slippery else SolidKind.Normal
        add(SolidEntry(r, kind))
    }
    level.movingPlatforms.forEachIndexed { i, def ->
        add(SolidEntry(movingPlatformRect(def, timeSeconds), SolidKind.Normal, movingIndex = i))
    }
    level.cyclingPlatforms.forEach { def ->
        if (cyclingPlatformCollidable(def, timeSeconds)) {
            add(SolidEntry(RectObj(def.x, def.y, def.width, def.height), SolidKind.Normal))
        }
    }
    level.springPads.forEach { sp ->
        add(SolidEntry(sp.rect, SolidKind.Spring))
    }
    level.lockedDoors.forEach { door ->
        if (door.id !in unlockedDoorIds) {
            add(SolidEntry(door.rect, SolidKind.Normal))
        }
    }
    val dh = destructibleHits
    if (dh != null && dh.size == level.destructibleBlocks.size) {
        level.destructibleBlocks.forEachIndexed { i, def ->
            if (dh[i] > 0) {
                add(SolidEntry(def.rect, SolidKind.Normal, destructibleIndex = i))
            }
        }
    }
    val oss = oneShotCrumbleStartMs
    if (level.oneShotPlatforms.isNotEmpty()) {
        level.oneShotPlatforms.forEachIndexed { i, def ->
            val start = if (oss != null && i < oss.size) oss[i] else -1L
            val disappearAt = start + def.crumbleDelayMs
            val respawnAt = disappearAt + def.respawnDelayMs
            val inRespawnCooldown =
                start >= 0L && elapsedMs >= disappearAt && elapsedMs < respawnAt
            if (!inRespawnCooldown) {
                add(SolidEntry(def.rect, SolidKind.Normal, oneShotIndex = i))
            }
        }
    }
    val bgu = bridgeGroupActiveUntilMs
    if (bgu != null && level.timedBridgeSegments.isNotEmpty()) {
        level.timedBridgeSegments.forEach { seg ->
            val gid = seg.groupId
            if (gid >= 0 && gid < bgu.size && elapsedMs < bgu[gid]) {
                add(SolidEntry(seg.rect, SolidKind.Normal))
            }
        }
    }
}
