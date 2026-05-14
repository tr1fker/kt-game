package com.example.game.platformer.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import java.io.Closeable

private const val ASSET_DIR = "sounds"


class GameSoundPlayer(context: Context) : Closeable {

    @Volatile
    var soundEnabled: Boolean = true
        private set

    fun setSoundEnabled(enabled: Boolean) {
        soundEnabled = enabled
    }

    private val appContext = context.applicationContext
    private val pool: SoundPool = SoundPool.Builder()
        .setMaxStreams(12)
        .setAudioAttributes(
            AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()
        )
        .build()

    private val streamIds = IntArray(SoundEvent.entries.size) { -1 }

    init {
        SoundEvent.entries.forEach { ev ->
            val idx = ev.ordinal
            try {
                appContext.assets.openFd("$ASSET_DIR/${ev.fileName}").use { afd ->
                    val id = pool.load(afd, 1)
                    streamIds[idx] = id
                }
            } catch (_: Throwable) {
                streamIds[idx] = -1
            }
        }
    }

    fun play(event: SoundEvent, volume: Float = 1f) {
        if (!soundEnabled) return
        val sid = streamIds[event.ordinal]
        if (sid <= 0) return
        val v = volume.coerceIn(0f, 1f)
        pool.play(sid, v, v, 1, 0, 1f)
    }

    override fun close() {
        pool.release()
    }
}
