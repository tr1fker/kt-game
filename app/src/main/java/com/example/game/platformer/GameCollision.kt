package com.example.game.platformer

import kotlin.math.max
import kotlin.math.min

fun intersects(a: RectObj, b: RectObj): Boolean {
    return a.x < b.x + b.width &&
        a.x + a.width > b.x &&
        a.y < b.y + b.height &&
        a.y + a.height > b.y
}

private fun distSqPointToSegment(px: Float, py: Float, ax: Float, ay: Float, bx: Float, by: Float): Float {
    val abx = bx - ax
    val aby = by - ay
    val apx = px - ax
    val apy = py - ay
    val abLenSq = abx * abx + aby * aby
    if (abLenSq < 1e-4f) return apx * apx + apy * apy
    val t = ((apx * abx + apy * aby) / abLenSq).coerceIn(0f, 1f)
    val qx = ax + abx * t
    val qy = ay + aby * t
    val dx = px - qx
    val dy = py - qy
    return dx * dx + dy * dy
}


fun rectIntersectsThickSegment(
    rect: RectObj,
    ax: Float,
    ay: Float,
    bx: Float,
    by: Float,
    halfWidth: Float
): Boolean {
    val hw2 = halfWidth * halfWidth
    val cx = rect.x + rect.width * 0.5f
    val cy = rect.y + rect.height * 0.5f
    if (distSqPointToSegment(cx, cy, ax, ay, bx, by) <= hw2) return true
    val x1 = rect.x
    val y1 = rect.y
    val x2 = rect.x + rect.width
    val y2 = rect.y + rect.height
    if (distSqPointToSegment(x1, y1, ax, ay, bx, by) <= hw2) return true
    if (distSqPointToSegment(x2, y1, ax, ay, bx, by) <= hw2) return true
    if (distSqPointToSegment(x1, y2, ax, ay, bx, by) <= hw2) return true
    if (distSqPointToSegment(x2, y2, ax, ay, bx, by) <= hw2) return true
    if (pointInRect(ax, ay, rect)) return true
    if (pointInRect(bx, by, rect)) return true
    val pad = halfWidth * 1.25f
    val segBox = RectObj(
        min(ax, bx) - pad,
        min(ay, by) - pad,
        max(ax, bx) - min(ax, bx) + pad * 2f,
        max(ay, by) - min(ay, by) + pad * 2f
    )
    return intersects(rect, segBox)
}
