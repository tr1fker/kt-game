package com.example.game.platformer

import androidx.compose.ui.graphics.Color
import kotlin.math.hypot
import kotlin.random.Random


fun spawnWindParticle(
    particles: MutableList<Particle>,
    zone: WindZone
) {
    val x = Random.nextDouble(zone.x.toDouble(), (zone.x + zone.width).toDouble()).toFloat()
    val y = Random.nextDouble(zone.y.toDouble(), (zone.y + zone.height).toDouble()).toFloat()
    val len = hypot(zone.forceX.toDouble(), zone.forceY.toDouble()).toFloat().coerceAtLeast(1f)
    val dirX = zone.forceX / len
    val dirY = zone.forceY / len
    val stream = Random.nextDouble(70.0, 190.0).toFloat()
    val perp = Random.nextDouble(-32.0, 32.0).toFloat()
    val vx = dirX * stream - dirY * perp * 0.35f
    val vy = dirY * stream + dirX * perp * 0.35f
    val life = Random.nextDouble(0.28, 0.55).toFloat()
    particles.add(
        Particle(
            x = x,
            y = y,
            vx = vx,
            vy = vy,
            life = life,
            maxLife = life,
            size = Random.nextDouble(1.8, 3.6).toFloat(),
            color = Color(0xDDE8F9FF)
        )
    )
}


fun spawnCyclingBlockMaterialize(
    particles: MutableList<Particle>,
    rect: RectObj
) {
    val cx = rect.x + rect.width / 2f
    val cy = rect.y + rect.height / 2f
    repeat(26) {
        val ang = Random.nextDouble(0.0, kotlin.math.PI * 2)
        val c = kotlin.math.cos(ang).toFloat()
        val s = kotlin.math.sin(ang).toFloat()
        val ring = Random.nextDouble(14.0, 58.0).toFloat()
        val pull = Random.nextDouble(60.0, 165.0).toFloat()
        val sx = cx + c * ring
        val sy = cy + s * ring
        val vx = -c * pull + Random.nextDouble(-28.0, 28.0).toFloat()
        val vy = -s * pull + Random.nextDouble(-22.0, 22.0).toFloat()
        val life = Random.nextDouble(0.32, 0.55).toFloat()
        particles.add(
            Particle(
                x = sx,
                y = sy,
                vx = vx,
                vy = vy,
                life = life,
                maxLife = life,
                size = Random.nextDouble(2.0, 4.2).toFloat(),
                color = Color(0xFFFFF3C4)
            )
        )
    }
}


fun spawnCyclingBlockDissolve(
    particles: MutableList<Particle>,
    rect: RectObj
) {
    repeat(40) {
        val sx = rect.x + Random.nextDouble(0.0, rect.width.toDouble()).toFloat()
        val sy = rect.y + Random.nextDouble(0.0, rect.height.toDouble()).toFloat()
        val ang = Random.nextDouble(0.0, kotlin.math.PI * 2)
        val sp = Random.nextDouble(75.0, 255.0).toFloat()
        val vx = kotlin.math.cos(ang).toFloat() * sp
        val vy = kotlin.math.sin(ang).toFloat() * sp * 0.75f - Random.nextDouble(0.0, 90.0).toFloat()
        val life = Random.nextDouble(0.36, 0.65).toFloat()
        val warm = if (Random.nextBoolean()) Color(0xFFFFB74D) else Color(0xFFFFE082)
        particles.add(
            Particle(
                x = sx,
                y = sy,
                vx = vx,
                vy = vy,
                life = life,
                maxLife = life,
                size = Random.nextDouble(2.2, 5.0).toFloat(),
                color = warm
            )
        )
    }
}

fun spawnTeleportSparkle(
    particles: MutableList<Particle>,
    x: Float,
    y: Float
) {
    repeat(16) {
        val speed = Random.nextDouble(60.0, 200.0).toFloat()
        val angle = Random.nextDouble(0.0, Math.PI * 2).toFloat()
        val life = Random.nextDouble(0.25, 0.5).toFloat()
        particles.add(
            Particle(
                x = x + Random.nextDouble(-6.0, 6.0).toFloat(),
                y = y + Random.nextDouble(-6.0, 6.0).toFloat(),
                vx = kotlin.math.cos(angle) * speed,
                vy = kotlin.math.sin(angle) * speed,
                life = life,
                maxLife = life,
                size = Random.nextDouble(2.0, 4.5).toFloat(),
                color = Color(0xFFD6A4FF)
            )
        )
    }
}

fun spawnParticles(
    particles: MutableList<Particle>,
    x: Float,
    y: Float,
    color: Color,
    count: Int
) {
    repeat(count) {
        val speed = Random.nextDouble(90.0, 280.0).toFloat()
        val angle = Random.nextDouble(0.0, Math.PI * 2).toFloat()
        val vx = kotlin.math.cos(angle) * speed
        val vy = kotlin.math.sin(angle) * speed - 60f
        val life = Random.nextDouble(0.35, 0.7).toFloat()
        particles.add(
            Particle(
                x = x + Random.nextDouble(-4.0, 4.0).toFloat(),
                y = y + Random.nextDouble(-4.0, 4.0).toFloat(),
                vx = vx,
                vy = vy,
                life = life,
                maxLife = life,
                size = Random.nextDouble(2.0, 5.0).toFloat(),
                color = color
            )
        )
    }
}

private val confettiPalette = listOf(
    Color(0xFFFF6B9D),
    Color(0xFFFFE66D),
    Color(0xFF7BED9F),
    Color(0xFF74B9FF),
    Color(0xFFA29BFE),
    Color(0xFFFFD66B)
)

fun spawnConfettiBurst(particles: MutableList<Particle>, x: Float, y: Float, count: Int = 58) {
    repeat(count) {
        val life = Random.nextDouble(0.55, 1.15).toFloat()
        particles.add(
            Particle(
                x = x + Random.nextFloat() * 26f - 13f,
                y = y + Random.nextFloat() * 18f - 9f,
                vx = Random.nextFloat() * 420f - 210f,
                vy = Random.nextFloat() * -380f - 90f,
                life = life,
                maxLife = life,
                size = Random.nextFloat() * 4.2f + 2.2f,
                color = confettiPalette.random()
            )
        )
    }
}


fun spawnRunDustMotes(particles: MutableList<Particle>, footX: Float, footY: Float, dirX: Float) {
    repeat(2) {
        val life = Random.nextDouble(0.18, 0.38).toFloat()
        particles.add(
            Particle(
                x = footX + Random.nextFloat() * 10f - 5f,
                y = footY + Random.nextFloat() * 4f,
                vx = -dirX * Random.nextFloat() * 95f - 25f + Random.nextFloat() * 50f,
                vy = -Random.nextFloat() * 95f - 25f,
                life = life,
                maxLife = life,
                size = Random.nextFloat() * 2.2f + 1.2f,
                color = Color(0xFF9A8B7A)
            )
        )
    }
}
