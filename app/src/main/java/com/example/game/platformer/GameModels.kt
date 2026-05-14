package com.example.game.platformer

import androidx.compose.ui.graphics.Color

enum class PlatformerDestination {
    MainMenu,
    LevelSelect,
    TimeAttackLevelSelect,
    Shop,
    Achievements,
    Records,
    Statistics,
    MusicSettings,
    Playing,
    LevelComplete,
    GameComplete
}

data class RectObj(
    val x: Float,
    val y: Float,
    val width: Float,
    val height: Float
)

data class Particle(
    var x: Float,
    var y: Float,
    var vx: Float,
    var vy: Float,
    var life: Float,
    val maxLife: Float,
    val size: Float,
    val color: Color
)

enum class MovingPlatformMotion {
    Horizontal,
    Vertical,
    Circular
}


data class MovingPlatformDef(
    val anchorX: Float,
    val anchorY: Float,
    val width: Float,
    val height: Float,
    val motion: MovingPlatformMotion,
    val range: Float,
    val periodSeconds: Float,
    val phase01: Float = 0f,
    val color: Color = Color(0xFF9B59B6)
)


data class CyclingPlatformDef(
    val x: Float,
    val y: Float,
    val width: Float,
    val height: Float,
    val visibleSeconds: Float,
    val hiddenSeconds: Float,
    val phaseOffsetSeconds: Float = 0f,
    val fadeInSeconds: Float = 0.14f,
    val fadeOutSeconds: Float = 0.18f
)


typealias CyclingHazardDef = CyclingPlatformDef


data class PressurePlateDef(
    val rect: RectObj,
    val bridgeGroupId: Int,
    val openDurationSec: Float = 3.2f
)


data class TimedBridgeSegmentDef(
    val groupId: Int,
    val rect: RectObj
)


data class WindZone(
    val x: Float,
    val y: Float,
    val width: Float,
    val height: Float,
    val forceX: Float,
    val forceY: Float
)

data class TeleportPair(
    val from: RectObj,
    val to: RectObj
)

data class SpringPad(
    val rect: RectObj,
    val jumpImpulse: Float = -620f
)


data class KeyPickup(val rect: RectObj, val doorId: Int)


data class LockedDoor(val id: Int, val rect: RectObj)


data class DestructibleBlockDef(val rect: RectObj, val hitsTotal: Int = 3)


data class OneShotPlatformDef(
    val rect: RectObj,
    val crumbleDelayMs: Long = 520L,
    val respawnDelayMs: Long = 2000L
)


data class ChargingLaserDef(
    val emitterX: Float,
    val emitterY: Float,
    
    val activationRadiusPx: Float = 200f,
    val chargeSeconds: Float = 2.6f,
    
    val emitterMarkerRadiusWorld: Float = 18f,
    val coreColor: Color = Color(0xFFFF1744),
    val warnGlowColor: Color = Color(0xFF40C4FF)
)

enum class SolidKind {
    Normal,
    Slippery,
    Spring
}

data class SolidEntry(
    val rect: RectObj,
    val kind: SolidKind,
    val movingIndex: Int = -1,
    val destructibleIndex: Int = -1,
    val oneShotIndex: Int = -1
)

data class Level(
    val startX: Float,
    val startY: Float,
    val platforms: List<RectObj>,
    val hazards: List<RectObj>,
    val checkpoints: List<RectObj>,
    val goal: RectObj,
    val movingPlatforms: List<MovingPlatformDef> = emptyList(),
    val cyclingPlatforms: List<CyclingPlatformDef> = emptyList(),
    val slipperyPlatforms: List<RectObj> = emptyList(),
    val windZones: List<WindZone> = emptyList(),
    val teleports: List<TeleportPair> = emptyList(),
    val springPads: List<SpringPad> = emptyList(),
    val keyPickups: List<KeyPickup> = emptyList(),
    val lockedDoors: List<LockedDoor> = emptyList(),
    val destructibleBlocks: List<DestructibleBlockDef> = emptyList(),
    val oneShotPlatforms: List<OneShotPlatformDef> = emptyList(),
    val cyclingHazards: List<CyclingHazardDef> = emptyList(),
    
    val chargingLasers: List<ChargingLaserDef> = emptyList(),
    val pressurePlates: List<PressurePlateDef> = emptyList(),
    val timedBridgeSegments: List<TimedBridgeSegmentDef> = emptyList(),
    
    val coinPickups: List<RectObj> = emptyList(),
    
    val slipperyGroundVelocityRetainPerTick: Float = 0.935f,
    
    val slipperyMaxHorizontalSpeedFactor: Float = 1f,
    
    val airHorizontalVelocityRetainPerTick: Float = 0.94f,
    
    val timeLimitSeconds: Int? = null,
    
    val visibilityRadiusWorld: Float? = null
)

data class LevelResult(
    val elapsedCentiseconds: Int,
    val deaths: Int,
    val stars: Int,
    val bestElapsedCentiseconds: Int,
    val bestStars: Int,
    val isNewRecord: Boolean,
    
    val coinsCollectedThisRun: Int = 0
)

data class BestLevelRecord(
    val bestElapsedCentiseconds: Int,
    val bestStars: Int
)

data class BestRecordUpdate(
    val record: BestLevelRecord,
    val isNewRecord: Boolean
)
