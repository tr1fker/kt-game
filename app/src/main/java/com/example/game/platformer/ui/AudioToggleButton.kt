package com.example.game.platformer.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import kotlin.math.min


@Composable
fun AudioToggleButton(
    audioEnabled: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier,
    iconTint: Color = Color(0xFFE8EEF5),
    containerWhenOn: Color = Color(0xCC3D5368),
    containerWhenOff: Color = Color(0xCC2A323C)
) {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val pressScale by animateFloatAsState(
        targetValue = if (pressed) 0.93f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessHigh
        ),
        label = "audioPress"
    )
    val bg by animateColorAsState(
        targetValue = if (audioEnabled) containerWhenOn else containerWhenOff,
        animationSpec = tween(320, easing = FastOutSlowInEasing),
        label = "audioBg"
    )
    val waves = rememberInfiniteTransition(label = "audioWaves")
    val wavePulse by waves.animateFloat(
        initialValue = 0.38f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(780, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "wavePulse"
    )

    Box(
        modifier = modifier
            .size(52.dp)
            .graphicsLayer {
                scaleX = pressScale
                scaleY = pressScale
            }
            .clip(RoundedCornerShape(14.dp))
            .background(bg)
            .clickable(
                interactionSource = interaction,
                indication = ripple(color = Color.White.copy(alpha = 0.2f)),
                onClick = onToggle
            ),
        contentAlignment = Alignment.Center
    ) {
        AnimatedContent(
            targetState = audioEnabled,
            transitionSpec = {
                (
                    fadeIn(tween(240, easing = FastOutSlowInEasing)) +
                        scaleIn(
                            initialScale = 0.78f,
                            animationSpec = tween(240, easing = FastOutSlowInEasing)
                        )
                    ) togetherWith (
                    fadeOut(tween(200)) +
                        scaleOut(
                            targetScale = 0.82f,
                            animationSpec = tween(200)
                        )
                    )
            },
            label = "audioGlyph"
        ) { on ->
            Canvas(modifier = Modifier.size(28.dp)) {
                drawAudioGlyph(on, iconTint, if (on) wavePulse else 1f)
            }
        }
    }
}

private fun DrawScope.drawAudioGlyph(
    enabled: Boolean,
    color: Color,
    waveAlpha: Float
) {
    val d = min(size.width, size.height)
    val stroke = d * 0.09f
    val cx = size.width * 0.30f
    val cy = size.height * 0.5f
    drawCircle(color = color, radius = d * 0.13f, center = Offset(cx, cy))
    val mouthX = cx + d * 0.10f
    val cone = Path().apply {
        moveTo(mouthX, cy - d * 0.20f)
        lineTo(mouthX, cy + d * 0.20f)
        lineTo(size.width * 0.52f, cy + d * 0.30f)
        lineTo(size.width * 0.52f, cy - d * 0.30f)
        close()
    }
    drawPath(cone, color, style = Fill)
    if (enabled) {
        val arcBaseX = size.width * 0.54f
        for (i in 0..2) {
            val r = d * (0.11f + i * 0.08f)
            val shift = i * d * 0.05f
            val alpha = (waveAlpha * (1f - i * 0.22f)).coerceIn(0.15f, 1f)
            drawArc(
                color = color.copy(alpha = alpha),
                startAngle = -68f,
                sweepAngle = 136f,
                useCenter = false,
                topLeft = Offset(arcBaseX + shift - r * 0.2f, cy - r),
                size = Size(r * 2f, r * 2f),
                style = Stroke(width = stroke, cap = StrokeCap.Round)
            )
        }
    } else {
        val pad = d * 0.16f
        drawLine(
            brush = SolidColor(color.copy(alpha = 0.92f)),
            start = Offset(pad, pad),
            end = Offset(size.width - pad, size.height - pad),
            strokeWidth = stroke * 1.55f,
            cap = StrokeCap.Round
        )
    }
}
