package com.example.game.platformer.ui

import android.os.SystemClock
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.game.platformer.localization.AppLanguage
import com.example.game.platformer.localization.formatCentisecondsLocalized
import com.example.game.platformer.localization.gameStrings
import com.example.game.platformer.audio.SoundEvent
import com.example.game.platformer.addPlayTimeMs
import com.example.game.platformer.AchievementEvents
import com.example.game.platformer.ChargingLaserDef
import com.example.game.platformer.Level
import com.example.game.platformer.LevelResult
import com.example.game.platformer.computeLevelContentBounds
import com.example.game.platformer.smoothCameraBlend
import com.example.game.platformer.Particle
import com.example.game.platformer.RectObj
import com.example.game.platformer.SolidKind
import com.example.game.platformer.calculateStars
import com.example.game.platformer.collectSolidEntries
import com.example.game.platformer.cyclingPlatformCollidable
import com.example.game.platformer.cyclingPlatformDrawAlpha
import com.example.game.platformer.intersects
import com.example.game.platformer.rectIntersectsThickSegment
import com.example.game.platformer.movingPlatformRect
import com.example.game.platformer.movingPlatformVelocity
import com.example.game.platformer.pointInRect
import com.example.game.platformer.windZoneToRect
import com.example.game.platformer.slipperyContains
import com.example.game.platformer.springImpulseForRect
import com.example.game.platformer.spawnCyclingBlockDissolve
import com.example.game.platformer.spawnCyclingBlockMaterialize
import com.example.game.platformer.spawnConfettiBurst
import com.example.game.platformer.spawnParticles
import com.example.game.platformer.spawnRunDustMotes
import com.example.game.platformer.spawnTeleportSparkle
import com.example.game.platformer.spawnWindParticle
import kotlin.collections.ArrayDeque
import kotlin.math.abs
import kotlin.math.exp
import kotlin.math.hypot
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin
import kotlin.random.Random
import kotlinx.coroutines.delay


private fun stepTowardHorizontalVelocity(
    current: Float,
    target: Float,
    maxAccelPerSecondSq: Float,
    dt: Float
): Float {
    val maxStep = maxAccelPerSecondSq * dt
    return current + (target - current).coerceIn(-maxStep, maxStep)
}

private fun inflateRect(r: RectObj, pad: Float): RectObj =
    RectObj(r.x - pad, r.y - pad, r.width + pad * 2f, r.height + pad * 2f)

private fun DrawScope.drawDisc(color: Color, center: Offset, radius: Float) {
    drawOval(
        color = color,
        topLeft = Offset(center.x - radius, center.y - radius),
        size = Size(radius * 2f, radius * 2f)
    )
}

@Composable
fun GameScreen(
    levelIndex: Int,
    level: Level,
    onBack: () -> Unit,
    onLevelCompleted: (LevelResult) -> Unit,
    playerMainColor: Color = Color(0xFF42A5F5),
    playerAccentColor: Color = Color(0xFF90CAF9),
    totalLevelsInGame: Int = 20,
    onAchievementUnlocked: (List<String>) -> Unit = {},
    onTimeAttackRetry: () -> Unit = {},
    
    onPauseToMainMenu: () -> Unit = {},
    audioEnabled: Boolean = true,
    onToggleAudio: () -> Unit = {},
    appLanguage: AppLanguage
) {
    
    val worldWidth = 1000f
    val worldHeight = 600f
    val appContext = LocalContext.current.applicationContext
    val gameSounds = LocalGameSounds.current
    val s = gameStrings(appLanguage)

    var playerX by remember(levelIndex) { mutableFloatStateOf(level.startX) }
    var playerY by remember(levelIndex) { mutableFloatStateOf(level.startY) }
    var respawnX by remember(levelIndex) { mutableFloatStateOf(level.startX) }
    var respawnY by remember(levelIndex) { mutableFloatStateOf(level.startY) }
    var velocityX by remember(levelIndex) { mutableFloatStateOf(0f) }
    var velocityY by remember(levelIndex) { mutableFloatStateOf(0f) }
    var onGround by remember(levelIndex) { mutableStateOf(false) }
    var jumpQueued by remember(levelIndex) { mutableStateOf(false) }
    var checkpointIndex by remember(levelIndex) { mutableIntStateOf(-1) }
    var checkpointFlashIndex by remember(levelIndex) { mutableIntStateOf(-1) }
    var checkpointFlashUntilMs by remember(levelIndex) { mutableLongStateOf(0L) }
    var deathsCount by remember(levelIndex) { mutableIntStateOf(0) }
    var elapsedMs by remember(levelIndex) { mutableLongStateOf(0L) }
    var startedAtMs by remember(levelIndex) { mutableLongStateOf(SystemClock.elapsedRealtime()) }
    var timeRunOut by remember(levelIndex) { mutableStateOf(false) }
    var isPaused by remember(levelIndex) { mutableStateOf(false) }
    val isPausedRef = rememberUpdatedState(isPaused)
    LaunchedEffect(timeRunOut, level.timeLimitSeconds) {
        if (timeRunOut && level.timeLimitSeconds != null) {
            gameSounds?.play(SoundEvent.GAME_TIME_UP)
        }
    }
    LaunchedEffect(levelIndex) {
        while (true) {
            delay(1000)
            if (!isPausedRef.value) {
                addPlayTimeMs(appContext, 1000L)
            }
        }
    }
    val particles = remember(levelIndex) { mutableStateListOf<Particle>() }
    var unlockedDoorIds by remember(levelIndex) { mutableStateOf(emptySet<Int>()) }
    var doorHintUntilMs by remember(levelIndex) { mutableLongStateOf(0L) }
    val destructibleHits = remember(levelIndex) {
        mutableStateListOf<Int>().apply { level.destructibleBlocks.forEach { add(it.hitsTotal) } }
    }
    val destructibleAlpha = remember(levelIndex) {
        mutableStateListOf<Float>().apply { level.destructibleBlocks.forEach { add(1f) } }
    }
    val oneShotCrumbleStartMs = remember(levelIndex) {
        mutableStateListOf<Long>().apply { repeat(level.oneShotPlatforms.size) { add(-1L) } }
    }
    val bridgeGroupCount =
        kotlin.math.max(
            level.pressurePlates.maxOfOrNull { it.bridgeGroupId } ?: -1,
            level.timedBridgeSegments.maxOfOrNull { it.groupId } ?: -1
        ) + 1
    val bridgeActiveUntilMs = remember(levelIndex, bridgeGroupCount) {
        LongArray(bridgeGroupCount.coerceAtLeast(0)) { 0L }
    }
    val pressurePlatePressAnim = remember(levelIndex) {
        mutableStateListOf<Float>().apply { repeat(level.pressurePlates.size) { add(0f) } }
    }
    val pressurePlateWasTouching = remember(levelIndex) {
        BooleanArray(level.pressurePlates.size)
    }
    var screenShakeX by remember(levelIndex) { mutableFloatStateOf(0f) }
    var screenShakeY by remember(levelIndex) { mutableFloatStateOf(0f) }
    var spikeDeathFreezeFrames by remember(levelIndex) { mutableIntStateOf(0) }
    var wasOnSlippery by remember(levelIndex) { mutableStateOf(false) }
    var teleportCooldownSec by remember(levelIndex) { mutableFloatStateOf(0f) }
    var windSpawnAcc by remember(levelIndex) { mutableFloatStateOf(0f) }
    
    var springBounceChain by remember(levelIndex) { mutableFloatStateOf(1f) }
    val cyclingPrevDrawAlpha = remember(levelIndex, level.cyclingPlatforms.size) {
        FloatArray(level.cyclingPlatforms.size) { 0f }
    }
    val cyclingHazardPrevDrawAlpha = remember(levelIndex, level.cyclingHazards.size) {
        FloatArray(level.cyclingHazards.size) { 0f }
    }
    val laserCharge = remember(levelIndex) {
        FloatArray(level.chargingLasers.size)
    }
    val laserWarnPlayed = remember(levelIndex) { BooleanArray(level.chargingLasers.size) }
    var collectedCoinIndices by remember(levelIndex) { mutableStateOf(emptySet<Int>()) }

    val playerWidth = 40f
    val playerHeight = 40f
    
    var camSmoothX by remember(levelIndex) { mutableFloatStateOf(level.startX + playerWidth / 2f) }
    var camSmoothY by remember(levelIndex) { mutableFloatStateOf(level.startY + playerHeight / 2f) }
    var playerSquashX by remember(levelIndex) { mutableFloatStateOf(1f) }
    var playerSquashY by remember(levelIndex) { mutableFloatStateOf(1f) }
    var zoomPulse by remember(levelIndex) { mutableFloatStateOf(0f) }
    var pendingLandingFx by remember(levelIndex) { mutableFloatStateOf(0f) }
    var dustRunAcc by remember(levelIndex) { mutableFloatStateOf(0f) }
    val motionTrail = remember(levelIndex) { ArrayDeque<Pair<Float, Float>>(10) }
    
    val moveSpeed = 450f
    val jumpSpeed = 720f
    val gravity = 1850f

    val leftInteraction = remember { MutableInteractionSource() }
    val rightInteraction = remember { MutableInteractionSource() }
    val leftPressed by leftInteraction.collectIsPressedAsState()
    val rightPressed by rightInteraction.collectIsPressedAsState()
    var keyboardLeftPressed by remember(levelIndex) { mutableStateOf(false) }
    var keyboardRightPressed by remember(levelIndex) { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    val contentBounds = remember(levelIndex) {
        computeLevelContentBounds(level, worldWidth, worldHeight)
    }
    val introBlend = key(levelIndex) {
        val progress by animateFloatAsState(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1150, easing = FastOutSlowInEasing),
            label = "levelIntroCamera"
        )
        progress
    }

    LaunchedEffect(levelIndex) {
        focusRequester.requestFocus()
    }

    LaunchedEffect(levelIndex) {
        
        val frameDelayMs = 11L
        while (true) {
            val dt = 1f / 60f
            if (!isPausedRef.value) {
                elapsedMs = SystemClock.elapsedRealtime() - startedAtMs
            }

            if (spikeDeathFreezeFrames > 0) {
                spikeDeathFreezeFrames--
                if (spikeDeathFreezeFrames > 0) {
                    screenShakeX = Random.nextDouble(-7.0, 7.0).toFloat()
                    screenShakeY = Random.nextDouble(-5.0, 5.0).toFloat()
                } else {
                    screenShakeX = 0f
                    screenShakeY = 0f
                    playerX = respawnX
                    playerY = respawnY
                    velocityX = 0f
                    velocityY = 0f
                    onGround = false
                    for (li in level.chargingLasers.indices) {
                        laserCharge[li] = 0f
                        laserWarnPlayed[li] = false
                    }
                }
                for (i in particles.lastIndex downTo 0) {
                    val particle = particles[i]
                    particle.x += particle.vx * dt
                    particle.y += particle.vy * dt
                    particle.vy += 580f * dt
                    particle.life -= dt
                    if (particle.life <= 0f) {
                        particles.removeAt(i)
                    }
                }
                val freezeTimeSec = elapsedMs / 1000f
                for (i in level.cyclingPlatforms.indices) {
                    cyclingPrevDrawAlpha[i] = cyclingPlatformDrawAlpha(level.cyclingPlatforms[i], freezeTimeSec)
                }
                for (i in level.cyclingHazards.indices) {
                    cyclingHazardPrevDrawAlpha[i] = cyclingPlatformDrawAlpha(level.cyclingHazards[i], freezeTimeSec)
                }
                delay(frameDelayMs)
                continue
            }

            if (isPausedRef.value) {
                delay(frameDelayMs)
                continue
            }

            if (timeRunOut) {
                delay(frameDelayMs)
                continue
            }

            val levelTimeSec = elapsedMs / 1000f
            if (teleportCooldownSec > 0f) {
                teleportCooldownSec = max(0f, teleportCooldownSec - dt)
            }

            val leftHeld = (leftPressed || keyboardLeftPressed) && !(rightPressed || keyboardRightPressed)
            val rightHeld = (rightPressed || keyboardRightPressed) && !(leftPressed || keyboardLeftPressed)
            val iceSpeed = moveSpeed * level.slipperyMaxHorizontalSpeedFactor.coerceIn(0.5f, 2f)
            val airOrIceHorizontal = !onGround || wasOnSlippery
            val horizontalApproachAccel = 4800f
            velocityX = if (airOrIceHorizontal) {
                when {
                    leftHeld && !rightHeld -> {
                        val target = if (wasOnSlippery && onGround) -iceSpeed else -moveSpeed
                        stepTowardHorizontalVelocity(velocityX, target, horizontalApproachAccel, dt)
                    }
                    rightHeld && !leftHeld -> {
                        val target = if (wasOnSlippery && onGround) iceSpeed else moveSpeed
                        stepTowardHorizontalVelocity(velocityX, target, horizontalApproachAccel, dt)
                    }
                    else -> when {
                        !onGround -> {
                            val r = level.airHorizontalVelocityRetainPerTick.coerceIn(0.5f, 0.999f)
                            val v = velocityX * r
                            if (abs(v) < 22f) 0f else v
                        }
                        wasOnSlippery -> {
                            val r = level.slipperyGroundVelocityRetainPerTick.coerceIn(0.5f, 0.999f)
                            val v = velocityX * r
                            if (abs(v) < 14f) 0f else v
                        }
                        else -> velocityX
                    }
                }
            } else {
                when {
                    leftHeld && !rightHeld -> -moveSpeed
                    rightHeld && !leftHeld -> moveSpeed
                    else -> 0f
                }
            }

            if (jumpQueued && onGround) {
                velocityY = -jumpSpeed
                onGround = false
                springBounceChain = 1f
                playerSquashX = 1.09f
                playerSquashY = 0.72f
                gameSounds?.play(SoundEvent.GAME_JUMP)
            }
            jumpQueued = false

            velocityY = min(1680f, velocityY + gravity * dt)

            val playerWindRect = RectObj(playerX, playerY, playerWidth, playerHeight)
            level.windZones.forEach { z ->
                val zr = windZoneToRect(z)
                if (intersects(playerWindRect, zr)) {
                    velocityX += z.forceX * dt
                    velocityY += z.forceY * dt
                }
            }
            velocityX = velocityX.coerceIn(-1450f, 1450f)

            if (level.windZones.isNotEmpty()) {
                windSpawnAcc += dt
                while (windSpawnAcc >= 0.066f) {
                    windSpawnAcc -= 0.066f
                    val z = level.windZones[Random.nextInt(level.windZones.size)]
                    spawnWindParticle(particles, z)
                }
            } else {
                windSpawnAcc = 0f
            }

            for (i in particles.lastIndex downTo 0) {
                val particle = particles[i]
                var inWind = false
                level.windZones.forEach { z ->
                    val zr = windZoneToRect(z)
                    if (pointInRect(particle.x, particle.y, zr)) {
                        inWind = true
                        val len = kotlin.math.hypot(z.forceX.toDouble(), z.forceY.toDouble()).toFloat().coerceAtLeast(1f)
                        val ax = z.forceX / len * 520f
                        val ay = z.forceY / len * 520f
                        particle.vx += ax * dt
                        particle.vy += ay * dt
                    }
                }
                particle.x += particle.vx * dt
                particle.y += particle.vy * dt
                particle.vy += (if (inWind) 180f else 580f) * dt
                particle.life -= dt
                if (particle.life <= 0f) {
                    particles.removeAt(i)
                }
            }

            val vyEnteringCollision = velocityY
            val wasGroundedBeforeCollision = onGround
            playerX += velocityX * dt
            playerY += velocityY * dt

            var playerRect = RectObj(playerX, playerY, playerWidth, playerHeight)
            onGround = false
            var standingSlippery = false

            for (i in level.oneShotPlatforms.indices) {
                if (i >= oneShotCrumbleStartMs.size) continue
                val t = oneShotCrumbleStartMs[i]
                if (t < 0L) continue
                val def = level.oneShotPlatforms[i]
                val respawnAt = t + def.crumbleDelayMs + def.respawnDelayMs
                if (elapsedMs >= respawnAt) {
                    oneShotCrumbleStartMs[i] = -1L
                    val cx = def.rect.x + def.rect.width * 0.5f
                    val cy = def.rect.y + def.rect.height * 0.5f
                    spawnParticles(
                        particles = particles,
                        x = cx,
                        y = cy,
                        color = Color(0xFFFFF59D),
                        count = 12
                    )
                }
            }

            val solids = collectSolidEntries(
                level,
                levelTimeSec,
                unlockedDoorIds,
                destructibleHits,
                elapsedMs,
                oneShotCrumbleStartMs,
                bridgeActiveUntilMs
            )
            solids.forEach { entry ->
                val platform = entry.rect
                if (intersects(playerRect, platform)) {
                    val overlapLeft = (playerRect.x + playerRect.width) - platform.x
                    val overlapRight = (platform.x + platform.width) - playerRect.x
                    val overlapTop = (playerRect.y + playerRect.height) - platform.y
                    val overlapBottom = (platform.y + platform.height) - playerRect.y

                    val minOverlap = min(
                        min(overlapLeft, overlapRight),
                        min(overlapTop, overlapBottom)
                    )

                    when (minOverlap) {
                        overlapTop -> {
                            playerY = platform.y - playerHeight
                            if (entry.kind == SolidKind.Spring) {
                                val incoming = vyEnteringCollision.coerceAtLeast(0f)
                                if (incoming < 58f && wasGroundedBeforeCollision) {
                                    velocityY = 0f
                                    onGround = true
                                    springBounceChain = 1f
                                } else {
                                    val base = springImpulseForRect(level.springPads, platform)
                                    val extraLift = -min(820f, incoming * 0.62f)
                                    val merged = min(base, base + extraLift)
                                    val bounced = merged * springBounceChain
                                    springBounceChain *= 0.82f
                                    val settle =
                                        (incoming < 48f && springBounceChain < 0.24f) ||
                                            springBounceChain < 0.055f
                                    if (settle || abs(bounced) < 115f) {
                                        velocityY = 0f
                                        onGround = true
                                        springBounceChain = 1f
                                        if (!wasGroundedBeforeCollision && incoming > 180f) {
                                            pendingLandingFx = max(pendingLandingFx, incoming * 0.55f)
                                        }
                                    } else {
                                        velocityY = bounced.coerceAtLeast(-1400f)
                                        onGround = false
                                        gameSounds?.play(SoundEvent.GAME_SPRING)
                                        if (abs(bounced) > 520f) {
                                            screenShakeX += Random.nextFloat() * 5f - 2.5f
                                            screenShakeY += Random.nextFloat() * 4f - 2f
                                        }
                                    }
                                }
                            } else {
                                springBounceChain = 1f
                                velocityY = 0f
                                onGround = true
                                if (!wasGroundedBeforeCollision && vyEnteringCollision > 210f) {
                                    pendingLandingFx = max(pendingLandingFx, vyEnteringCollision)
                                }
                            }
                            if (entry.kind == SolidKind.Slippery) {
                                standingSlippery = true
                            }
                            if (entry.movingIndex >= 0) {
                                val def = level.movingPlatforms[entry.movingIndex]
                                val (pvx, pvy) = movingPlatformVelocity(def, levelTimeSec)
                                playerX += pvx * dt
                                playerY += pvy * dt
                            }
                            if (entry.destructibleIndex >= 0) {
                                val di = entry.destructibleIndex
                                if (di < destructibleHits.size && destructibleHits[di] > 0 && vyEnteringCollision > 165f) {
                                    destructibleHits[di] = destructibleHits[di] - 1
                                    gameSounds?.play(SoundEvent.GAME_BLOCK_HIT)
                                    spawnParticles(
                                        particles = particles,
                                        x = platform.x + platform.width * 0.5f,
                                        y = platform.y,
                                        color = Color(0xFFE0D0BC),
                                        count = 7
                                    )
                                    if (destructibleHits[di] == 0) {
                                        gameSounds?.play(SoundEvent.GAME_BLOCK_BREAK)
                                        spawnParticles(
                                            particles = particles,
                                            x = platform.x + platform.width * 0.5f,
                                            y = platform.y + platform.height * 0.5f,
                                            color = Color(0xFF9E8B7A),
                                            count = 12
                                        )
                                    }
                                }
                            }
                            if (entry.oneShotIndex >= 0) {
                                val oi = entry.oneShotIndex
                                if (oi < oneShotCrumbleStartMs.size && oneShotCrumbleStartMs[oi] < 0L) {
                                    oneShotCrumbleStartMs[oi] = elapsedMs
                                    spawnParticles(
                                        particles = particles,
                                        x = platform.x + platform.width * 0.5f,
                                        y = platform.y + platform.height * 0.45f,
                                        color = Color(0xFFFFB74D),
                                        count = 8
                                    )
                                }
                            }
                        }

                        overlapBottom -> {
                            playerY = platform.y + platform.height
                            velocityY = max(0f, velocityY)
                            if (entry.destructibleIndex >= 0) {
                                val di = entry.destructibleIndex
                                if (di < destructibleHits.size && destructibleHits[di] > 0 && vyEnteringCollision < -165f) {
                                    destructibleHits[di] = destructibleHits[di] - 1
                                    gameSounds?.play(SoundEvent.GAME_BLOCK_HIT)
                                    spawnParticles(
                                        particles = particles,
                                        x = platform.x + platform.width * 0.5f,
                                        y = platform.y + platform.height,
                                        color = Color(0xFFD8C8B4),
                                        count = 7
                                    )
                                    if (destructibleHits[di] == 0) {
                                        gameSounds?.play(SoundEvent.GAME_BLOCK_BREAK)
                                        spawnParticles(
                                            particles = particles,
                                            x = platform.x + platform.width * 0.5f,
                                            y = platform.y + platform.height * 0.5f,
                                            color = Color(0xFF9E8B7A),
                                            count = 12
                                        )
                                    }
                                }
                            }
                        }

                        overlapLeft -> {
                            playerX = platform.x - playerWidth
                        }

                        else -> {
                            playerX = platform.x + platform.width
                        }
                    }
                    playerRect = RectObj(playerX, playerY, playerWidth, playerHeight)
                }
            }

            wasOnSlippery = standingSlippery

            val keyPickupRect = RectObj(playerX, playerY, playerWidth, playerHeight)
            for (kp in level.keyPickups) {
                if (kp.doorId !in unlockedDoorIds && intersects(keyPickupRect, kp.rect)) {
                    unlockedDoorIds = unlockedDoorIds + kp.doorId
                    gameSounds?.play(SoundEvent.GAME_KEY_PICKUP)
                    spawnParticles(
                        particles = particles,
                        x = kp.rect.x + kp.rect.width / 2f,
                        y = kp.rect.y + kp.rect.height / 2f,
                        color = Color(0xFFFFF176),
                        count = 20
                    )
                    break
                }
            }
            val doorHintPad = 28f
            val touchingLockedDoorHint = level.lockedDoors.any { door ->
                door.id !in unlockedDoorIds &&
                    intersects(keyPickupRect, inflateRect(door.rect, doorHintPad))
            }
            if (touchingLockedDoorHint) {
                doorHintUntilMs = elapsedMs + 2800L
            }

            val coinPickupRect = RectObj(playerX, playerY, playerWidth, playerHeight)
            for (i in level.coinPickups.indices) {
                if (i in collectedCoinIndices) continue
                val c = level.coinPickups[i]
                if (intersects(coinPickupRect, c)) {
                    collectedCoinIndices = collectedCoinIndices + i
                    gameSounds?.play(SoundEvent.GAME_COIN)
                    val cx = c.x + c.width / 2f
                    val cy = c.y + c.height / 2f
                    spawnParticles(
                        particles = particles,
                        x = cx,
                        y = cy,
                        color = Color(0xFFFFD700),
                        count = 16
                    )
                    spawnParticles(
                        particles = particles,
                        x = cx,
                        y = cy,
                        color = Color(0xFFFFF59D),
                        count = 12
                    )
                    onAchievementUnlocked(AchievementEvents.onCoinPicked(appContext, totalLevelsInGame, appLanguage))
                }
            }

            if (teleportCooldownSec <= 0f) {
                val cr = RectObj(playerX, playerY, playerWidth, playerHeight)
                for (pair in level.teleports) {
                    if (intersects(cr, pair.from)) {
                        playerX = pair.to.x + pair.to.width / 2f - playerWidth / 2f
                        playerY = pair.to.y + pair.to.height - playerHeight
                        val fx = pair.from.x + pair.from.width / 2f
                        val fy = pair.from.y + pair.from.height / 2f
                        val tx = pair.to.x + pair.to.width / 2f
                        val ty = pair.to.y + pair.to.height / 2f
                        spawnTeleportSparkle(particles, fx, fy)
                        spawnTeleportSparkle(particles, tx, ty)
                        gameSounds?.play(SoundEvent.GAME_TELEPORT)
                        teleportCooldownSec = 0.55f
                        velocityX *= 0.25f
                        velocityY = 0f
                        break
                    }
                }
            }

            for (i in level.cyclingPlatforms.indices) {
                val def = level.cyclingPlatforms[i]
                val a = cyclingPlatformDrawAlpha(def, levelTimeSec)
                val p = cyclingPrevDrawAlpha[i]
                val r = RectObj(def.x, def.y, def.width, def.height)
                if (p < 0.12f && a > 0.2f) {
                    spawnCyclingBlockMaterialize(particles, r)
                }
                if (p > 0.18f && a < 0.1f) {
                    spawnCyclingBlockDissolve(particles, r)
                }
                cyclingPrevDrawAlpha[i] = a
            }
            for (i in level.cyclingHazards.indices) {
                val def = level.cyclingHazards[i]
                val a = cyclingPlatformDrawAlpha(def, levelTimeSec)
                val p = cyclingHazardPrevDrawAlpha[i]
                val r = RectObj(def.x, def.y, def.width, def.height)
                if (p < 0.12f && a > 0.22f) {
                    spawnCyclingBlockMaterialize(particles, r)
                }
                if (p > 0.18f && a < 0.1f) {
                    spawnCyclingBlockDissolve(particles, r)
                }
                cyclingHazardPrevDrawAlpha[i] = a
            }

            val playerRectEarly = RectObj(playerX, playerY, playerWidth, playerHeight)
            var laserFatal = false
            val pcxEarly = playerX + playerWidth * 0.5f
            val pcyEarly = playerY + playerHeight * 0.5f
            for (i in level.chargingLasers.indices) {
                val def = level.chargingLasers[i]
                val dist = hypot(pcxEarly - def.emitterX, pcyEarly - def.emitterY)
                val inZone = dist <= def.activationRadiusPx
                if (inZone) {
                    laserCharge[i] += dt / def.chargeSeconds.coerceAtLeast(0.08f)
                } else {
                    laserCharge[i] = 0f
                    laserWarnPlayed[i] = false
                }
                val raw = laserCharge[i]
                if (inZone && raw >= 0.4f && !laserWarnPlayed[i]) {
                    gameSounds?.play(SoundEvent.GAME_LASER_WARN)
                    laserWarnPlayed[i] = true
                }
                if (raw > 0.08f && raw < 0.99f && Random.nextFloat() < 0.22f) {
                    val t = Random.nextFloat()
                    val sx = def.emitterX + (pcxEarly - def.emitterX) * t
                    val sy = def.emitterY + (pcyEarly - def.emitterY) * t
                    val abx = pcxEarly - def.emitterX
                    val aby = pcyEarly - def.emitterY
                    val plen = hypot(abx, aby).coerceAtLeast(1f)
                    val nx = -aby / plen
                    val ny = abx / plen
                    val ox = nx * (Random.nextFloat() * 16f - 8f)
                    val oy = ny * (Random.nextFloat() * 16f - 8f)
                    spawnParticles(
                        particles = particles,
                        x = sx + ox,
                        y = sy + oy,
                        color = def.warnGlowColor,
                        count = 2
                    )
                }
                if (raw >= 1f) {
                    gameSounds?.play(SoundEvent.GAME_LASER_FIRE)
                    laserFatal = true
                    laserCharge[i] = 0f
                } else {
                    val capped = raw.coerceIn(0f, 1f)
                    laserCharge[i] = capped
                    if (capped >= 0.93f && rectIntersectsThickSegment(
                            playerRectEarly,
                            def.emitterX,
                            def.emitterY,
                            pcxEarly,
                            pcyEarly,
                            10f
                        )
                    ) {
                        laserFatal = true
                        laserCharge[i] = 0f
                    }
                }
            }

            val correctedRect = RectObj(playerX, playerY, playerWidth, playerHeight)
            level.checkpoints.forEachIndexed { index, checkpoint ->
                if (index > checkpointIndex && intersects(correctedRect, checkpoint)) {
                    checkpointIndex = index
                    checkpointFlashIndex = index
                    checkpointFlashUntilMs = elapsedMs + 350L
                    respawnX = checkpoint.x
                    respawnY = checkpoint.y - playerHeight
                    gameSounds?.play(SoundEvent.GAME_CHECKPOINT)
                    spawnParticles(
                        particles = particles,
                        x = checkpoint.x + checkpoint.width / 2f,
                        y = checkpoint.y + checkpoint.height / 2f,
                        color = Color(0xFF7EF0AC),
                        count = 22
                    )
                    zoomPulse = 0.09f
                }
            }

            level.pressurePlates.forEachIndexed { i, plate ->
                val gid = plate.bridgeGroupId
                if (i < pressurePlatePressAnim.size) {
                    val touching = intersects(correctedRect, plate.rect)
                    if (i < pressurePlateWasTouching.size) {
                        if (touching && !pressurePlateWasTouching[i]) {
                            gameSounds?.play(SoundEvent.GAME_PRESSURE_PLATE)
                        }
                        pressurePlateWasTouching[i] = touching
                    }
                    val target = if (touching) 1f else 0f
                    val cur = pressurePlatePressAnim[i]
                    val k = if (target > cur) 26f else 15f
                    pressurePlatePressAnim[i] = cur + (target - cur) * (1f - exp(-k * dt))
                }
                if (gid >= 0 && gid < bridgeActiveUntilMs.size && intersects(correctedRect, plate.rect)) {
                    val addMs = (plate.openDurationSec * 1000f).toLong().coerceIn(250L, 120_000L)
                    bridgeActiveUntilMs[gid] = maxOf(bridgeActiveUntilMs[gid], elapsedMs + addMs)
                }
            }

            val hitHazard = laserFatal ||
                level.hazards.any { intersects(correctedRect, it) } ||
                level.cyclingHazards.any { def ->
                    val r = RectObj(def.x, def.y, def.width, def.height)
                    cyclingPlatformCollidable(def, levelTimeSec) && intersects(correctedRect, r)
                }
            val fellOff = playerY > worldHeight + 80f
            if (hitHazard) {
                gameSounds?.play(SoundEvent.GAME_DEATH)
                deathsCount += 1
                onAchievementUnlocked(AchievementEvents.onPlayerDeath(appContext, totalLevelsInGame, appLanguage))
                spawnParticles(
                    particles = particles,
                    x = playerX + playerWidth / 2f,
                    y = playerY + playerHeight / 2f,
                    color = Color(0xFFFFAB40),
                    count = 16
                )
                spawnParticles(
                    particles = particles,
                    x = playerX + playerWidth / 2f,
                    y = playerY + playerHeight / 2f,
                    color = Color(0xFFFF6B6B),
                    count = 28
                )
                velocityX = 0f
                velocityY = 0f
                spikeDeathFreezeFrames = 32
                delay(frameDelayMs)
                continue
            }
            if (fellOff) {
                gameSounds?.play(SoundEvent.GAME_DEATH)
                deathsCount += 1
                onAchievementUnlocked(AchievementEvents.onPlayerDeath(appContext, totalLevelsInGame, appLanguage))
                spawnParticles(
                    particles = particles,
                    x = playerX + playerWidth / 2f,
                    y = playerY + playerHeight / 2f,
                    color = Color(0xFFFF6B6B),
                    count = 18
                )
                screenShakeX = 0f
                screenShakeY = 0f
                playerX = respawnX
                playerY = respawnY
                velocityX = 0f
                velocityY = 0f
                onGround = false
                for (li in level.chargingLasers.indices) {
                    laserCharge[li] = 0f
                    laserWarnPlayed[li] = false
                }
                delay(220)
                continue
            }

            if (intersects(correctedRect, level.goal)) {
                gameSounds?.play(SoundEvent.GAME_GOAL)
                val gx = level.goal.x + level.goal.width / 2f
                val gy = level.goal.y + level.goal.height / 2f
                spawnConfettiBurst(particles, gx, gy)
                repeat(26) {
                    for (i in particles.lastIndex downTo 0) {
                        val particle = particles[i]
                        particle.x += particle.vx * dt
                        particle.y += particle.vy * dt
                        particle.vy += 420f * dt
                        particle.life -= dt
                        if (particle.life <= 0f) {
                            particles.removeAt(i)
                        }
                    }
                    delay(frameDelayMs)
                }
                val elapsedCentiseconds = (elapsedMs / 10L).toInt()
                val runStars = calculateStars(elapsedCentiseconds, deathsCount)
                onLevelCompleted(
                    LevelResult(
                        elapsedCentiseconds = elapsedCentiseconds,
                        deaths = deathsCount,
                        stars = runStars,
                        bestElapsedCentiseconds = elapsedCentiseconds,
                        bestStars = runStars,
                        isNewRecord = false,
                        coinsCollectedThisRun = collectedCoinIndices.size
                    )
                )
                return@LaunchedEffect
            }

            val limitMsTotal = level.timeLimitSeconds?.let { it * 1000L }
            if (limitMsTotal != null && elapsedMs >= limitMsTotal) {
                timeRunOut = true
            }
            if (timeRunOut) {
                delay(frameDelayMs)
                continue
            }

            for (i in level.destructibleBlocks.indices) {
                if (i < destructibleHits.size && i < destructibleAlpha.size) {
                    if (destructibleHits[i] == 0 && destructibleAlpha[i] > 0f) {
                        destructibleAlpha[i] = max(0f, destructibleAlpha[i] - dt * 2.5f)
                    }
                }
            }

            if (pendingLandingFx > 300f) {
                val landVol = (pendingLandingFx / 720f).coerceIn(0.28f, 1f)
                gameSounds?.play(SoundEvent.GAME_LAND, landVol)
                spawnParticles(
                    particles = particles,
                    x = playerX + playerWidth * 0.5f,
                    y = playerY + playerHeight * 0.88f,
                    color = Color(0xFF7D6E5F),
                    count = 9
                )
            }
            if (pendingLandingFx > 460f) {
                screenShakeX += Random.nextFloat() * 8f - 4f
                screenShakeY += Random.nextFloat() * 5f - 2.5f
            }
            pendingLandingFx = 0f

            if (onGround && ((leftHeld && !rightHeld) || (!leftHeld && rightHeld)) && abs(velocityX) > 115f) {
                dustRunAcc += dt
                if (dustRunAcc > 0.052f) {
                    dustRunAcc = 0f
                    val dir = if (velocityX > 0f) 1f else -1f
                    spawnRunDustMotes(
                        particles,
                        playerX + playerWidth * 0.5f + dir * 10f,
                        playerY + playerHeight - 3f,
                        dir
                    )
                }
            } else {
                dustRunAcc *= 0.65f
            }

            val tcxTrail = playerX + playerWidth / 2f
            val tcyTrail = playerY + playerHeight / 2f
            motionTrail.addLast(Pair(tcxTrail, tcyTrail))
            while (motionTrail.size > 8) {
                motionTrail.removeFirst()
            }

            playerSquashX += (1f - playerSquashX) * (1f - exp(-12f * dt))
            playerSquashY += (1f - playerSquashY) * (1f - exp(-12f * dt))
            if (zoomPulse > 0.0008f) {
                zoomPulse *= 0.86f
            } else {
                zoomPulse = 0f
            }

            val targetCamX = playerX + playerWidth / 2f
            val targetCamY = playerY + playerHeight / 2f
            val camSnap = 1f - exp(-7.8f * dt)
            camSmoothX += (targetCamX - camSmoothX) * camSnap
            camSmoothY += (targetCamY - camSmoothY) * camSnap
            delay(frameDelayMs)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF101319))
    ) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .focusRequester(focusRequester)
            .focusable()
            .onPreviewKeyEvent { keyEvent ->
                when (keyEvent.type) {
                    KeyEventType.KeyDown -> {
                        when (keyEvent.key) {
                            Key.Escape -> {
                                jumpQueued = false
                                if (isPaused) {
                                    gameSounds?.play(SoundEvent.GAME_PAUSE_CLOSE)
                                    startedAtMs = SystemClock.elapsedRealtime() - elapsedMs
                                    isPaused = false
                                } else {
                                    gameSounds?.play(SoundEvent.GAME_PAUSE_OPEN)
                                    keyboardLeftPressed = false
                                    keyboardRightPressed = false
                                    isPaused = true
                                }
                                true
                            }

                            Key.DirectionLeft, Key.A -> {
                                if (!isPaused) keyboardLeftPressed = true
                                true
                            }

                            Key.DirectionRight, Key.D -> {
                                if (!isPaused) keyboardRightPressed = true
                                true
                            }

                            Key.Spacebar, Key.DirectionUp, Key.W -> {
                                if (!isPaused) jumpQueued = true
                                true
                            }

                            else -> false
                        }
                    }

                    KeyEventType.KeyUp -> {
                        when (keyEvent.key) {
                            Key.DirectionLeft, Key.A -> {
                                keyboardLeftPressed = false
                                true
                            }

                            Key.DirectionRight, Key.D -> {
                                keyboardRightPressed = false
                                true
                            }

                            else -> false
                        }
                    }

                    else -> false
                }
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "${s.levelWord}$levelIndex${s.gameHudTime}${
                            formatCentisecondsLocalized((elapsedMs / 10L).toInt(), s.secondsShort)
                        }${s.gameHudDeaths}$deathsCount",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    if (level.timeLimitSeconds != null) {
                        val limMs = level.timeLimitSeconds * 1000L
                        val remMs = (limMs - elapsedMs).coerceAtLeast(0L)
                        val remSec = (remMs / 1000L).toInt()
                        val remCol =
                            if (remMs <= 10_000L) Color(0xFFFF5252) else Color(0xFFFFB74D)
                        Text(
                            text = "${s.gameTimeLimitPrefix}${level.timeLimitSeconds}${s.gameTimeLimitMiddle}${remSec / 60}:${(remSec % 60).toString().padStart(2, '0')}",
                            color = remCol,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                    if (level.coinPickups.isNotEmpty()) {
                        Text(
                            "${s.gameCoinsRun}${collectedCoinIndices.size}/${level.coinPickups.size}",
                            color = Color(0xFFFFE082),
                            fontWeight = FontWeight.Medium,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    PlatformerHudButton(
                        onClick = {
                            jumpQueued = false
                            keyboardLeftPressed = false
                            keyboardRightPressed = false
                            isPaused = true
                        },
                        text = s.pause,
                        clickSound = SoundEvent.GAME_PAUSE_OPEN
                    )
                    PlatformerHudButton(onClick = onBack, text = s.levelsButton)
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(vertical = 4.dp)
                .background(Color(0xFF1B2230), RoundedCornerShape(12.dp))
                .graphicsLayer {
                    translationX = screenShakeX
                    translationY = screenShakeY
                }
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val canvasAspect = size.width / size.height
                val st = smoothCameraBlend(introBlend)
                val introCx = contentBounds.x + contentBounds.width / 2f
                val introCy = contentBounds.y + contentBounds.height / 2f
                val playerCx = playerX + playerWidth / 2f
                val playerCy = playerY + playerHeight / 2f

                val needW = (contentBounds.width * 1.08f).coerceAtLeast(140f)
                val needH = (contentBounds.height * 1.08f).coerceAtLeast(100f)
                var introW = max(needW, needH * canvasAspect)
                var introH = introW / canvasAspect
                if (introH < needH) {
                    introH = needH
                    introW = introH * canvasAspect
                }
                introW = introW.coerceAtMost(worldWidth)
                introH = introH.coerceAtMost(worldHeight)
                if (introW < needW.coerceAtMost(worldWidth)) {
                    introW = needW.coerceAtMost(worldWidth)
                    introH = (introW / canvasAspect).coerceAtMost(worldHeight)
                }
                if (introH < needH.coerceAtMost(worldHeight)) {
                    introH = needH.coerceAtMost(worldHeight)
                    introW = (introH * canvasAspect).coerceAtMost(worldWidth)
                }

                
                val followH = 575f
                val followW = (followH * canvasAspect)
                    .coerceIn(420f, worldWidth * 0.995f)
                    .coerceAtLeast(320f)

                val viewW = introW + (followW - introW) * st
                val viewH = viewW / canvasAspect
                val introLerpCx = introCx + (playerCx - introCx) * st
                val introLerpCy = introCy + (playerCy - introCy) * st
                val useFollowCam = introBlend >= 0.992f
                val ccx = if (useFollowCam) camSmoothX else introLerpCx
                val ccy = if (useFollowCam) camSmoothY else introLerpCy

                val scale = size.width / viewW
                val scaleDraw = scale * (1f + zoomPulse)
                val offsetX = size.width / 2f - ccx * scaleDraw
                val offsetY = size.height / 2f - ccy * scaleDraw

                fun worldRectToScreen(rect: RectObj): Pair<Offset, Size> {
                    val topLeft = Offset(offsetX + rect.x * scaleDraw, offsetY + rect.y * scaleDraw)
                    val rectSize = Size(rect.width * scaleDraw, rect.height * scaleDraw)
                    return Pair(topLeft, rectSize)
                }

                val levelTimeDraw = elapsedMs / 1000f
                val playerRectWorld = RectObj(playerX, playerY, playerWidth, playerHeight)

                drawSkyParallaxAndClouds(ccx, scaleDraw, levelTimeDraw)

                level.windZones.forEach { z ->
                    val (topLeft, rectSize) = worldRectToScreen(RectObj(z.x, z.y, z.width, z.height))
                    drawRect(color = Color(0x28A8DAFF), topLeft = topLeft, size = rectSize)
                }

                level.platforms.forEach { platform ->
                    val (topLeft, rectSize) = worldRectToScreen(platform)
                    if (slipperyContains(level.slipperyPlatforms, platform)) {
                        drawGradientPlatform(
                            topLeft,
                            rectSize,
                            Color(0xFF9BD8EC),
                            Color(0xFF5BA8C8),
                            Color(0xFF2E6F86).copy(alpha = 0.85f)
                        )
                    } else {
                        drawGradientPlatform(
                            topLeft,
                            rectSize,
                            Color(0xFF9BC98A),
                            Color(0xFF5E8A4E),
                            Color(0xFF2D4A26).copy(alpha = 0.88f)
                        )
                    }
                }

                level.timedBridgeSegments.forEach { seg ->
                    val gid = seg.groupId
                    val active =
                        gid >= 0 && gid < bridgeActiveUntilMs.size && elapsedMs < bridgeActiveUntilMs[gid]
                    val remaining = if (active) bridgeActiveUntilMs[gid] - elapsedMs else 0L
                    val urgency =
                        if (active && remaining in 1L..900L) {
                            0.9f + 0.1f * sin((elapsedMs * 0.021).toDouble()).toFloat()
                        } else {
                            1f
                        }
                    val (tl, sz) = worldRectToScreen(seg.rect)
                    if (active) {
                        drawGradientPlatform(
                            tl,
                            sz,
                            Color(0xFFB2DFDB).copy(alpha = urgency),
                            Color(0xFF26A69A).copy(alpha = urgency),
                            Color(0xFF004D40).copy(alpha = 0.9f * urgency)
                        )
                        drawRect(
                            color = Color(0x44FFFFFF),
                            topLeft = Offset(tl.x + 4f, tl.y + 4f),
                            size = Size((sz.width - 8f).coerceAtLeast(2f), sz.height * 0.28f)
                        )
                    } else {
                        drawRoundRect(
                            color = Color(0xFF455A64).copy(alpha = 0.2f),
                            topLeft = tl,
                            size = sz,
                            cornerRadius = CornerRadius(5f, 5f)
                        )
                    }
                }

                level.pressurePlates.forEachIndexed { i, plate ->
                    val press = if (i < pressurePlatePressAnim.size) pressurePlatePressAnim[i] else 0f
                    val (tl, sz) = worldRectToScreen(plate.rect)
                    val squash = 0.38f * press
                    val drawH = sz.height * (1f - squash)
                    val drawTop = tl.y + sz.height * squash
                    val drawLeft = tl.x + sz.width * 0.04f * press
                    val drawW = sz.width * (1f - 0.08f * press)
                    val top = lerp(Color(0xFF5C6BC0), Color(0xFF9FA8DA), press)
                    val mid = lerp(Color(0xFF303F9F), Color(0xFF5C6BC0), press)
                    val bot = lerp(Color(0xFF1A237E), Color(0xFF3949AB), press).copy(alpha = 0.9f)
                    drawGradientPlatform(
                        Offset(drawLeft, drawTop),
                        Size(drawW, drawH),
                        top,
                        mid,
                        bot
                    )
                    drawRoundRect(
                        color = lerp(Color(0x33FFFFFF), Color(0x55FFFFFF), press),
                        topLeft = Offset(
                            drawLeft + drawW * 0.15f,
                            drawTop + drawH * 0.1f
                        ),
                        size = Size(drawW * 0.7f, drawH * 0.28f),
                        cornerRadius = CornerRadius(3f, 3f)
                    )
                    if (press > 0.08f) {
                        drawRoundRect(
                            color = Color(0xFF000000).copy(alpha = 0.22f * press),
                            topLeft = Offset(drawLeft + 2f, drawTop + drawH - 4f),
                            size = Size(drawW - 4f, 3f),
                            cornerRadius = CornerRadius(1.5f, 1.5f)
                        )
                    }
                }

                level.oneShotPlatforms.forEachIndexed { i, def ->
                    if (i >= oneShotCrumbleStartMs.size) return@forEachIndexed
                    val start = oneShotCrumbleStartMs[i]
                    val crumble = def.crumbleDelayMs
                    val disappearAt = start + crumble
                    val respawnAt = disappearAt + def.respawnDelayMs
                    if (start >= 0L && elapsedMs >= disappearAt && elapsedMs < respawnAt) {
                        return@forEachIndexed
                    }
                    val crumbT =
                        if (start < 0L) 0f
                        else ((elapsedMs - start).toFloat() / crumble.coerceAtLeast(1L)).coerceIn(0f, 1f)
                    val alpha = if (start < 0L) 1f else (1f - crumbT * 0.78f).coerceIn(0.16f, 1f)
                    val (tl, sz) = worldRectToScreen(def.rect)
                    drawGradientPlatform(
                        tl,
                        sz,
                        Color(0xFFFFD54F).copy(alpha = alpha),
                        Color(0xFFFF9800).copy(alpha = alpha),
                        Color(0xFFE65100).copy(alpha = alpha * 0.92f)
                    )
                }

                level.lockedDoors.forEach { door ->
                    val (tl, sz) = worldRectToScreen(door.rect)
                    if (door.id !in unlockedDoorIds) {
                        drawGradientPlatform(
                            tl,
                            sz,
                            Color(0xFF6D4C41),
                            Color(0xFF3E2723),
                            Color(0xFF1A1008).copy(alpha = 0.92f)
                        )
                    } else {
                        val touchOpened = intersects(playerRectWorld, door.rect)
                        val base = if (touchOpened) Color(0xFF1A1A1A) else Color(0xFF2C2C2C)
                        drawRoundRect(
                            color = base.copy(alpha = 0.72f),
                            topLeft = tl,
                            size = sz,
                            cornerRadius = CornerRadius(6f, 6f)
                        )
                        drawRoundRect(
                            color = Color(0xFF0D0D0D).copy(alpha = if (touchOpened) 0.55f else 0.35f),
                            topLeft = Offset(tl.x + 4f, tl.y + 4f),
                            size = Size((sz.width - 8f).coerceAtLeast(2f), (sz.height - 8f).coerceAtLeast(2f)),
                            cornerRadius = CornerRadius(4f, 4f)
                        )
                    }
                }
                level.destructibleBlocks.forEachIndexed { i, block ->
                    if (i >= destructibleAlpha.size) return@forEachIndexed
                    val alphaVis = destructibleAlpha[i]
                    if (alphaVis <= 0.02f) return@forEachIndexed
                    val (tl, sz) = worldRectToScreen(block.rect)
                    drawGradientPlatform(
                        tl,
                        sz,
                        Color(0xFFBCAAA4).copy(alpha = alphaVis),
                        Color(0xFF795548).copy(alpha = alphaVis),
                        Color(0xFF452B1F).copy(alpha = alphaVis * 0.95f)
                    )
                }
                level.keyPickups.forEach { kp ->
                    if (kp.doorId !in unlockedDoorIds) {
                        val (tl, sz) = worldRectToScreen(kp.rect)
                        drawRoundRect(
                            color = Color(0xFFFFCA28),
                            topLeft = tl,
                            size = sz,
                            cornerRadius = CornerRadius(5f, 5f)
                        )
                        drawRoundRect(
                            color = Color(0xFFFFEE58).copy(alpha = 0.55f),
                            topLeft = Offset(tl.x + sz.width * 0.2f, tl.y + sz.height * 0.2f),
                            size = Size(sz.width * 0.35f, sz.height * 0.35f),
                            cornerRadius = CornerRadius(3f, 3f)
                        )
                    }
                }

                level.movingPlatforms.forEach { def ->
                    val mr = movingPlatformRect(def, levelTimeDraw)
                    val (topLeft, rectSize) = worldRectToScreen(mr)
                    val c = def.color
                    drawGradientPlatform(
                        topLeft,
                        rectSize,
                        c.lighten(1.12f),
                        c.darken(0.82f),
                        c.darken(0.48f).copy(alpha = 0.9f)
                    )
                }

                level.cyclingPlatforms.forEach { def ->
                    val a = cyclingPlatformDrawAlpha(def, levelTimeDraw)
                    if (a > 0.02f) {
                        val r = RectObj(def.x, def.y, def.width, def.height)
                        val (topLeft, rectSize) = worldRectToScreen(r)
                        drawRect(
                            color = Color(0x66FFD54F).copy(alpha = a * 0.9f),
                            topLeft = Offset(topLeft.x - 5f, topLeft.y - 4f),
                            size = Size(rectSize.width + 10f, rectSize.height + 8f)
                        )
                        drawGradientPlatform(
                            topLeft,
                            rectSize,
                            Color(0xFFFFB74D).copy(alpha = a),
                            Color(0xFFE65100).copy(alpha = a),
                            Color(0xFFBF360C).copy(alpha = a * 0.9f)
                        )
                        drawRect(
                            color = Color(0x44FFFFFF).copy(alpha = a * 0.55f),
                            topLeft = Offset(topLeft.x + rectSize.width * 0.12f, topLeft.y + rectSize.height * 0.15f),
                            size = Size(rectSize.width * 0.35f, rectSize.height * 0.25f)
                        )
                    }
                }

                level.hazards.forEach { hazard ->
                    val (topLeft, rectSize) = worldRectToScreen(hazard)
                    drawGradientPlatform(
                        topLeft,
                        rectSize,
                        Color(0xFFE57373),
                        Color(0xFFC62828),
                        Color(0xFF5C1010).copy(alpha = 0.9f)
                    )
                }

                level.cyclingHazards.forEach { def ->
                    val a = cyclingPlatformDrawAlpha(def, levelTimeDraw)
                    if (a > 0.02f) {
                        val pulse = (0.88 + 0.12 * sin((levelTimeDraw * 24.0).toDouble())).toFloat()
                        val wobbleY = (sin((levelTimeDraw * 16.5).toDouble()) * 2.6 * a).toFloat()
                        val r = RectObj(def.x, def.y + wobbleY, def.width, def.height)
                        val (topLeft, rectSize) = worldRectToScreen(r)
                        drawRect(
                            color = Color(0x55FF8A80).copy(alpha = a * pulse),
                            topLeft = Offset(topLeft.x - 6f, topLeft.y - 5f),
                            size = Size(rectSize.width + 12f, rectSize.height + 10f)
                        )
                        drawGradientPlatform(
                            topLeft,
                            rectSize,
                            Color(0xFFFFAB91).copy(alpha = a * pulse),
                            Color(0xFFFF1744).copy(alpha = a * pulse),
                            Color(0xFF870000).copy(alpha = a * 0.94f * pulse)
                        )
                        drawRect(
                            color = Color(0x55FFFFFF).copy(alpha = a * 0.5f),
                            topLeft = Offset(topLeft.x + rectSize.width * 0.1f, topLeft.y),
                            size = Size(rectSize.width * 0.8f, rectSize.height * 0.26f)
                        )
                    }
                }

                level.chargingLasers.forEachIndexed { i, def ->
                    fun wpt(x: Float, y: Float) = Offset(offsetX + x * scaleDraw, offsetY + y * scaleDraw)
                    val sEm = wpt(def.emitterX, def.emitterY)
                    val mr = def.emitterMarkerRadiusWorld * scaleDraw
                    val pulseM = (0.92f + 0.08f * sin((levelTimeDraw * 5.0 + i).toDouble())).toFloat()
                    drawCircle(
                        color = Color(0xFF1A237E).copy(alpha = 0.92f),
                        radius = mr * 1.15f * pulseM,
                        center = sEm
                    )
                    drawCircle(
                        color = def.warnGlowColor.copy(alpha = 0.88f),
                        radius = mr * 0.72f,
                        center = sEm
                    )
                    drawCircle(
                        color = Color(0xFFFFF59D).copy(alpha = 0.95f),
                        radius = mr * 0.35f,
                        center = sEm
                    )
                    drawCircle(
                        color = def.warnGlowColor.copy(
                            alpha = (0.06f + 0.05f * (0.5f + 0.5f * sin((levelTimeDraw * 2.2).toDouble()).toFloat()))
                                .coerceIn(0.04f, 0.13f)
                        ),
                        radius = def.activationRadiusPx * scaleDraw,
                        center = sEm
                    )
                }

                level.teleports.forEach { pair ->
                    val (a, asz) = worldRectToScreen(pair.from)
                    drawGradientPlatform(a, asz, Color(0xFFCE93D8), Color(0xFF7B1FA2), Color(0xFF4A148C).copy(alpha = 0.85f))
                    val (b, bsz) = worldRectToScreen(pair.to)
                    drawGradientPlatform(b, bsz, Color(0xFF81D4FA), Color(0xFF1976D2), Color(0xFF0D47A1).copy(alpha = 0.85f))
                }

                level.springPads.forEach { sp ->
                    val (topLeft, rectSize) = worldRectToScreen(sp.rect)
                    drawGradientPlatform(
                        topLeft,
                        rectSize,
                        Color(0xFFDCEDC8),
                        Color(0xFF8BC34A),
                        Color(0xFF33691E).copy(alpha = 0.88f)
                    )
                    drawRect(
                        color = Color(0xFF689F38).copy(alpha = 0.75f),
                        topLeft = Offset(topLeft.x, topLeft.y + rectSize.height * 0.55f),
                        size = Size(rectSize.width, rectSize.height * 0.45f)
                    )
                }

                level.checkpoints.forEachIndexed { index, checkpoint ->
                    val (topLeft, rectSize) = worldRectToScreen(checkpoint)
                    if (index <= checkpointIndex) {
                        drawGradientPlatform(
                            topLeft,
                            rectSize,
                            Color(0xFF81C784),
                            Color(0xFF388E3C),
                            Color(0xFF1B5E20).copy(alpha = 0.88f)
                        )
                    } else {
                        drawGradientPlatform(
                            topLeft,
                            rectSize,
                            Color(0xFF4C8C6F),
                            Color(0xFF2E5C45),
                            Color(0xFF142E22).copy(alpha = 0.9f)
                        )
                    }
                }
                if (checkpointFlashIndex >= 0 && elapsedMs < checkpointFlashUntilMs) {
                    val checkpoint = level.checkpoints[checkpointFlashIndex]
                    val (topLeft, rectSize) = worldRectToScreen(checkpoint)
                    drawRoundRect(
                        color = Color(0x5599F0B8),
                        topLeft = Offset(topLeft.x - 8f, topLeft.y - 8f),
                        size = Size(rectSize.width + 16f, rectSize.height + 16f),
                        cornerRadius = CornerRadius(6f, 6f)
                    )
                    drawRoundRect(
                        color = Color(0x8899F0B8),
                        topLeft = Offset(topLeft.x - 4f, topLeft.y - 4f),
                        size = Size(rectSize.width + 8f, rectSize.height + 8f),
                        cornerRadius = CornerRadius(4f, 4f)
                    )
                }

                level.coinPickups.forEachIndexed { i, coinRect ->
                    if (i in collectedCoinIndices) return@forEachIndexed
                    val bob = sin((levelTimeDraw * 5.5 + i * 0.9).toDouble()).toFloat() * 3f * scaleDraw
                    val (tl, sz) = worldRectToScreen(coinRect)
                    val cx = tl.x + sz.width / 2f
                    val cy = tl.y + sz.height / 2f + bob
                    val r = min(sz.width, sz.height) * 0.42f
                    drawDisc(
                        color = Color(0xFFFFC107).copy(alpha = 0.35f),
                        center = Offset(cx, cy),
                        radius = r * 1.35f
                    )
                    drawDisc(
                        color = Color(0xFFFFD54F),
                        center = Offset(cx, cy),
                        radius = r
                    )
                    drawDisc(
                        color = Color(0xFFFFA000).copy(alpha = 0.55f),
                        center = Offset(cx - r * 0.12f, cy - r * 0.15f),
                        radius = r * 0.55f
                    )
                }

                val (goalOffset, goalSize) = worldRectToScreen(level.goal)
                drawGoalPulse(goalOffset, goalSize, levelTimeDraw)
                drawGradientPlatform(
                    goalOffset,
                    goalSize,
                    Color(0xFFFFF59D),
                    Color(0xFFFFB300),
                    Color(0xFFE65100).copy(alpha = 0.75f)
                )

                val playerRect = RectObj(playerX, playerY, playerWidth, playerHeight)
                val (playerOffset, playerSize) = worldRectToScreen(playerRect)
                val trailN = motionTrail.size.coerceAtLeast(1)
                var trailI = 0
                for (p in motionTrail) {
                    trailI++
                    val alpha = (trailI.toFloat() / trailN) * 0.22f
                    val cx = offsetX + p.first * scaleDraw
                    val cy = offsetY + p.second * scaleDraw
                    val tw = playerSize.width * 0.88f
                    val th = playerSize.height * 0.88f
                    drawRoundRect(
                        color = playerMainColor.copy(alpha = alpha),
                        topLeft = Offset(cx - tw / 2f, cy - th / 2f),
                        size = Size(tw, th),
                        cornerRadius = CornerRadius(2f, 2f)
                    )
                }
                drawPlayerShadow(
                    feetCenterScreen = Offset(playerOffset.x + playerSize.width * 0.5f, playerOffset.y + playerSize.height),
                    widthPx = playerSize.width * 1.2f
                )
                val pcx = playerOffset.x + playerSize.width * 0.5f
                val pcy = playerOffset.y + playerSize.height * 0.5f
                scale(
                    scaleX = playerSquashX,
                    scaleY = playerSquashY,
                    pivot = Offset(pcx, pcy)
                ) {
                    drawRoundRect(
                        color = playerMainColor,
                        topLeft = playerOffset,
                        size = playerSize,
                        cornerRadius = CornerRadius(2f, 2f)
                    )
                    drawRoundRect(
                        color = playerAccentColor.copy(alpha = 0.45f),
                        topLeft = Offset(
                            playerOffset.x + playerSize.width * 0.18f,
                            playerOffset.y + playerSize.height * 0.12f
                        ),
                        size = Size(playerSize.width * 0.38f, playerSize.height * 0.28f),
                        cornerRadius = CornerRadius(2f, 2f)
                    )
                }

                level.chargingLasers.forEachIndexed { i, def ->
                    fun wpt(x: Float, y: Float) = Offset(offsetX + x * scaleDraw, offsetY + y * scaleDraw)
                    val pcxv = playerX + playerWidth / 2f
                    val pcyv = playerY + playerHeight / 2f
                    val ch = laserCharge.getOrElse(i) { 0f }.coerceIn(0f, 1f)
                    val distEm = hypot(pcxv - def.emitterX, pcyv - def.emitterY)
                    val inZone = distEm <= def.activationRadiusPx
                    val intensity = if (inZone) max(ch, 0.16f).coerceAtMost(1f) else ch
                    if (intensity < 0.04f) return@forEachIndexed
                    val sEm = wpt(def.emitterX, def.emitterY)
                    val sHero = wpt(pcxv, pcyv)
                    val pulse = (1f + 0.12f * sin((levelTimeDraw * 24.0 + i * 2.1).toDouble())).toFloat()
                    val wBase = (6f + 20f * intensity) * scaleDraw * pulse
                    repeat(3) { layer ->
                        val w = wBase * (1f - layer * 0.3f)
                        val a = (0.12f + 0.55f * intensity) * (1f - layer * 0.38f)
                        drawLine(
                            color = def.warnGlowColor.copy(alpha = a.coerceIn(0.04f, 1f)),
                            start = sEm,
                            end = sHero,
                            strokeWidth = w.coerceAtLeast(1.5f),
                            cap = StrokeCap.Round
                        )
                    }
                    drawLine(
                        color = def.coreColor.copy(alpha = (0.35f + 0.65f * intensity).coerceIn(0f, 1f)),
                        start = sEm,
                        end = sHero,
                        strokeWidth = max(2.2f, wBase * 0.3f),
                        cap = StrokeCap.Round
                    )
                }

                particles.forEach { particle ->
                    val alpha = (particle.life / particle.maxLife).coerceIn(0f, 1f)
                    val px = offsetX + particle.x * scaleDraw
                    val py = offsetY + particle.y * scaleDraw
                    val r = particle.size * scaleDraw
                    drawDisc(
                        color = particle.color.copy(alpha = alpha * 0.2f),
                        center = Offset(px, py),
                        radius = r * 2.1f
                    )
                    drawDisc(
                        color = particle.color.copy(alpha = alpha),
                        center = Offset(px, py),
                        radius = r
                    )
                }

                val visWorld = level.visibilityRadiusWorld
                if (visWorld != null) {
                    drawPlayerVisibilitySpotlight(
                        holeCenter = Offset(pcx, pcy),
                        holeRadiusPx = visWorld * scaleDraw
                    )
                } else {
                    drawVignette()
                }
            }

            if (elapsedMs < doorHintUntilMs &&
                level.lockedDoors.any { it.id !in unlockedDoorIds }
            ) {
                Text(
                    text = s.doorLockedHint,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 12.dp, start = 12.dp, end = 12.dp)
                        .background(Color(0xF0222830), RoundedCornerShape(12.dp))
                        .padding(horizontal = 18.dp, vertical = 14.dp),
                    color = Color(0xFFFFF9C4),
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                PlatformerHudButton(
                    onClick = {},
                    text = s.moveLeft,
                    interactionSource = leftInteraction,
                    modifier = Modifier.width(112.dp).height(72.dp)
                )
                PlatformerHudButton(
                    onClick = {},
                    text = s.moveRight,
                    interactionSource = rightInteraction,
                    modifier = Modifier.width(112.dp).height(72.dp)
                )
            }
            PlatformerPrimaryButton(
                onClick = { jumpQueued = true },
                modifier = Modifier.size(width = 140.dp, height = 72.dp)
            ) {
                Text(s.jump, fontWeight = FontWeight.Bold, fontSize = 17.sp)
            }
        }
    }

        if (timeRunOut && level.timeLimitSeconds != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xE6000000)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(28.dp)
                ) {
                    Text(
                        s.timeUpTitle,
                        color = Color.White,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        if (level.timeLimitSeconds != null) {
                            String.format(s.timeUpBody, level.timeLimitSeconds)
                        } else {
                            s.timeUpBodyNoLimit
                        },
                        color = Color(0xFFE0E0E0),
                        fontSize = 15.sp,
                        modifier = Modifier.padding(top = 10.dp, bottom = 22.dp),
                        textAlign = TextAlign.Center
                    )
                    PlatformerPrimaryButton(onClick = onTimeAttackRetry) {
                        Text(s.retry, fontWeight = FontWeight.SemiBold)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    PlatformerSecondaryButton(onClick = onBack) {
                        Text(s.toList, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }

        if (isPaused) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xE6081018))
            ) {
                AudioToggleButton(
                    audioEnabled = audioEnabled,
                    onToggle = onToggleAudio,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(14.dp),
                    containerWhenOn = Color(0xE6506275),
                    containerWhenOff = Color(0xE63A4350)
                )
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(28.dp)
                ) {
                    Text(
                        s.pauseTitle,
                        color = Color.White,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        s.pauseHint,
                        color = Color(0xFFB0B8C4),
                        fontSize = 14.sp,
                        modifier = Modifier.padding(top = 8.dp, bottom = 22.dp),
                        textAlign = TextAlign.Center
                    )
                    PlatformerPrimaryButton(
                        onClick = {
                            startedAtMs = SystemClock.elapsedRealtime() - elapsedMs
                            jumpQueued = false
                            isPaused = false
                        },
                        clickSound = SoundEvent.GAME_PAUSE_CLOSE
                    ) {
                        Text(s.resume, fontWeight = FontWeight.SemiBold)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    PlatformerSecondaryButton(
                        onClick = {
                            isPaused = false
                            onPauseToMainMenu()
                        }
                    ) {
                        Text(s.toMainMenu, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}
