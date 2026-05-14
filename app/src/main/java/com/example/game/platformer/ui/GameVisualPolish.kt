package com.example.game.platformer.ui

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin


fun DrawScope.drawSkyParallaxAndClouds(
    cameraCenterWorldX: Float,
    worldToScreenScale: Float,
    timeSec: Float
) {
    drawRect(
        brush = Brush.verticalGradient(
            colors = listOf(
                Color(0xFF0A1628),
                Color(0xFF122238),
                Color(0xFF1A2D42),
                Color(0xFF1B2230)
            ),
            startY = 0f,
            endY = size.height
        ),
        size = size
    )

    val camShiftPx = (cameraCenterWorldX - 500f) * worldToScreenScale

    fun hillLayer(parallax: Float, baseY: Float, amp: Float, color: Color, phase: Float) {
        val shift = camShiftPx * parallax + phase * 40f
        val path = Path()
        val step = 36f
        var x = -120f
        path.moveTo(x, size.height + 40f)
        path.lineTo(x, baseY)
        while (x < size.width + 160f) {
            val wave = sin(((x + shift) * 0.014f + timeSec * 0.35f).toDouble()).toFloat() * amp +
                sin(((x + shift) * 0.027f).toDouble()).toFloat() * (amp * 0.35f)
            path.lineTo(x, baseY + wave)
            x += step
        }
        path.lineTo(x, size.height + 40f)
        path.close()
        drawPath(path, color)
    }

    hillLayer(0.06f, size.height * 0.58f, 22f, Color(0xFF141E2E), 0f)
    hillLayer(0.11f, size.height * 0.62f, 28f, Color(0xFF182436), 1.7f)
    hillLayer(0.19f, size.height * 0.68f, 34f, Color(0xFF1C2838), 3.1f)

    val cloudAlpha = 0.14f
    val drift = timeSec * 18f
    fun cloud(cx: Float, cy: Float, w: Float, h: Float) {
        drawOval(
            color = Color.White.copy(alpha = cloudAlpha),
            topLeft = Offset(cx, cy),
            size = Size(w, h)
        )
    }
    cloud((size.width * 0.15f + drift * 0.3f) % (size.width + 80f) - 40f, size.height * 0.12f + sin((timeSec * 0.4f).toDouble()).toFloat() * 6f, 120f, 36f)
    cloud((size.width * 0.55f - drift * 0.22f) % (size.width + 100f) - 20f, size.height * 0.18f + cos((timeSec * 0.35f).toDouble()).toFloat() * 8f, 160f, 44f)
    cloud((size.width * 0.82f + drift * 0.18f) % (size.width + 120f), size.height * 0.10f, 100f, 30f)
}

fun DrawScope.drawVignette() {
    val c = Offset(size.width / 2f, size.height / 2f)
    val r = max(size.width, size.height) * 0.72f
    drawRect(
        brush = Brush.radialGradient(
            colors = listOf(Color.Transparent, Color(0xDD000000)),
            center = c,
            radius = r
        ),
        topLeft = Offset.Zero,
        size = size
    )
}


fun DrawScope.drawPlayerVisibilitySpotlight(
    holeCenter: Offset,
    holeRadiusPx: Float,
    darknessColor: Color = Color.Black
) {
    val r = holeRadiusPx.coerceAtLeast(24f)
    val pad = 8f
    val path = Path().apply {
        fillType = PathFillType.EvenOdd
        addRect(Rect(-pad, -pad, size.width + pad, size.height + pad))
        addOval(
            Rect(
                holeCenter.x - r,
                holeCenter.y - r,
                holeCenter.x + r,
                holeCenter.y + r
            )
        )
    }
    drawPath(path, darknessColor, blendMode = BlendMode.SrcOver)
}

fun DrawScope.drawGradientPlatform(
    topLeft: Offset,
    rectSize: Size,
    topColor: Color,
    bottomColor: Color,
    outlineColor: Color
) {
    drawRoundRect(
        brush = Brush.verticalGradient(
            colors = listOf(topColor, bottomColor),
            startY = topLeft.y,
            endY = topLeft.y + rectSize.height
        ),
        topLeft = topLeft,
        size = rectSize,
        cornerRadius = CornerRadius(3f, 3f)
    )
    drawRoundRect(
        color = outlineColor,
        topLeft = topLeft,
        size = rectSize,
        cornerRadius = CornerRadius(3f, 3f),
        style = Stroke(width = max(1.5f, rectSize.width.coerceAtMost(rectSize.height) * 0.04f))
    )
}

fun DrawScope.drawPlayerShadow(feetCenterScreen: Offset, widthPx: Float, alpha: Float = 0.28f) {
    drawOval(
        color = Color.Black.copy(alpha = alpha),
        topLeft = Offset(feetCenterScreen.x - widthPx * 0.5f, feetCenterScreen.y - 5f),
        size = Size(widthPx, 14f)
    )
}

fun DrawScope.drawGoalPulse(goalTopLeft: Offset, goalSize: Size, timeSec: Float) {
    val pulse = 0.5f + 0.5f * sin((timeSec * 3.2f).toDouble()).toFloat()
    val pad = 10f + pulse * 8f
    drawRoundRect(
        color = Color(0xFFFFE08A).copy(alpha = 0.22f + 0.12f * pulse),
        topLeft = Offset(goalTopLeft.x - pad, goalTopLeft.y - pad),
        size = Size(goalSize.width + pad * 2f, goalSize.height + pad * 2f),
        cornerRadius = CornerRadius(8f, 8f)
    )
}

fun Color.darken(factor: Float): Color =
    copy(red = red * factor, green = green * factor, blue = blue * factor)

fun Color.lighten(factor: Float): Color =
    copy(
        red = min(1f, red * factor),
        green = min(1f, green * factor),
        blue = min(1f, blue * factor)
    )
