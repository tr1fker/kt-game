package com.example.game.platformer.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.Closeable


class GameMusicPlayer(context: Context) : Closeable {

    private val app = context.applicationContext

    private var player: MediaPlayer? = null

    @Volatile
    private var musicEnabled: Boolean = true

    @Volatile
    private var loadedPath: String? = null

    fun setMusicEnabled(enabled: Boolean) {
        musicEnabled = enabled
        if (!enabled) {
            runCatching { player?.pause() }
        }
    }

    suspend fun playMenuIfNeeded() = switchTo(BgmTrack.Menu)

    suspend fun playLevelIfNeeded() = switchTo(BgmTrack.Level)

    private enum class BgmTrack(val assetPath: String) {
        Menu("music/bgm_menu.ogg"),
        Level("music/bgm_level.ogg")
    }

    private suspend fun switchTo(track: BgmTrack) {
        if (!musicEnabled) {
            withContext(Dispatchers.Main) {
                runCatching { player?.pause() }
            }
            return
        }
        val path = track.assetPath
        if (loadedPath == path) {
            withContext(Dispatchers.Main) {
                try {
                    if (player?.isPlaying != true) player?.start()
                } catch (_: Exception) {
                }
            }
            return
        }

        withContext(Dispatchers.Main) {
            try {
                player?.pause()
                player?.stop()
                player?.reset()
            } catch (_: Exception) {
            }
        }

        val mp = withContext(Dispatchers.IO) {
            try {
                val p = player ?: MediaPlayer()
                p.reset()
                p.setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_GAME)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build()
                )
                app.assets.openFd(path).use { afd ->
                    p.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
                }
                p.isLooping = true
                p.prepare()
                loadedPath = path
                p
            } catch (_: Exception) {
                loadedPath = null
                runCatching { player?.release() }
                player = null
                null
            }
        }

        withContext(Dispatchers.Main) {
            if (mp != null) {
                player = mp
                if (musicEnabled) {
                    try {
                        mp.start()
                    } catch (_: Exception) {
                    }
                }
            }
        }
    }

    override fun close() {
        runCatching {
            player?.release()
        }
        player = null
        loadedPath = null
    }
}
