package com.example.game.platformer.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.Closeable

private enum class BgmContext {
    Menu,
    Level
}

class GameMusicPlayer(private val app: Context) : Closeable {

    private val musicJob = SupervisorJob()
    private val scope = CoroutineScope(musicJob + Dispatchers.Main.immediate)

    private var player: MediaPlayer? = null

    @Volatile
    private var musicEnabled: Boolean = true

    private var loadedPath: String? = null

    private var currentContext: BgmContext? = null

    private var playlist: List<String> = emptyList()

    private var queue: MutableList<String> = mutableListOf()

    private var queueIndex: Int = 0

    private var lastMenuSig: String? = null

    private var lastLevelSig: String? = null

    fun setMusicEnabled(enabled: Boolean) {
        musicEnabled = enabled
        if (!enabled) {
            runCatching { player?.pause() }
        }
    }

    /** Сброс кэша «уже играет тот же плейлист» — после смены настроек музыки. */
    fun clearBgmStateCache() {
        lastMenuSig = null
        lastLevelSig = null
    }

    /** Пауза фоновой музыки на время предпрослушивания трека в настройках. */
    fun pauseForPreview() {
        runCatching { player?.pause() }
    }

    /** Возобновить фон, если музыка включена и плеер настроен на трек. */
    fun tryResumeAfterPreview() {
        if (!musicEnabled) return
        runCatching {
            val p = player ?: return
            if (loadedPath != null) p.start()
        }
    }

    suspend fun playMenuIfNeeded() {
        switchToContext(BgmContext.Menu)
    }

    suspend fun playLevelIfNeeded() {
        switchToContext(BgmContext.Level)
    }

    private suspend fun switchToContext(ctx: BgmContext) {
        if (!musicEnabled) {
            withContext(Dispatchers.Main) {
                runCatching { player?.pause() }
            }
            return
        }

        val paths = when (ctx) {
            BgmContext.Menu -> loadMenuBgmPlaylist(app)
            BgmContext.Level -> loadLevelBgmPlaylist(app)
        }.distinct()

        val sig = paths.joinToString("|")
        when (ctx) {
            BgmContext.Menu -> {
                if (currentContext == BgmContext.Menu && sig == lastMenuSig && player?.isPlaying == true) {
                    return
                }
                lastMenuSig = sig
            }
            BgmContext.Level -> {
                if (currentContext == BgmContext.Level && sig == lastLevelSig && player?.isPlaying == true) {
                    return
                }
                lastLevelSig = sig
            }
        }

        currentContext = ctx

        if (paths.isEmpty()) {
            withContext(Dispatchers.Main) {
                runCatching {
                    player?.setOnCompletionListener(null)
                    player?.stop()
                    player?.release()
                }
                player = null
                loadedPath = null
                playlist = emptyList()
                queue.clear()
            }
            return
        }

        playlist = paths
        queue = paths.toMutableList()
        if (queue.size > 1) {
            queue.shuffle()
            if (queue.size >= 2) {
                var guard = 0
                while (guard < 8 && queue.first() == loadedPath && loadedPath != null) {
                    queue.shuffle()
                    guard++
                }
            }
        }
        queueIndex = 0

        playCurrentTrackFromQueue(startFresh = true)
    }

    private suspend fun playCurrentTrackFromQueue(startFresh: Boolean) {
        val path = queue.getOrNull(queueIndex) ?: return
        val single = queue.size <= 1

        if (!startFresh && loadedPath == path && player != null) {
            withContext(Dispatchers.Main) {
                try {
                    if (musicEnabled && player?.isPlaying != true) player?.start()
                } catch (_: Exception) {
                }
            }
            return
        }

        withContext(Dispatchers.Main) {
            runCatching {
                player?.setOnCompletionListener(null)
                player?.pause()
                player?.stop()
                player?.reset()
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
                p.setDataSourceForBgmTrack(app, path)
                p.isLooping = single
                if (!single) {
                    p.setOnCompletionListener { completed ->
                        if (completed != p) return@setOnCompletionListener
                        scope.launch {
                            advanceQueueAndPlay()
                        }
                    }
                } else {
                    p.setOnCompletionListener(null)
                }
                p.prepare()
                loadedPath = path
                p
            } catch (_: Exception) {
                loadedPath = null
                runCatching { player?.release() }
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

    private suspend fun advanceQueueAndPlay() {
        if (!musicEnabled) return
        if (queue.size <= 1) return

        queueIndex++
        if (queueIndex >= queue.size) {
            queueIndex = 0
            reshuffleQueueAvoidImmediateRepeat()
        }
        playCurrentTrackFromQueue(startFresh = true)
    }

    private fun reshuffleQueueAvoidImmediateRepeat() {
        val last = loadedPath
        val copy = playlist.toMutableList()
        if (copy.size >= 2) {
            copy.shuffle()
            var tries = 0
            while (tries < 12 && copy.first() == last) {
                copy.shuffle()
                tries++
            }
        }
        queue.clear()
        queue.addAll(copy)
    }

    override fun close() {
        musicJob.cancel()
        runCatching {
            player?.setOnCompletionListener(null)
            player?.release()
        }
        player = null
        loadedPath = null
        playlist = emptyList()
        queue.clear()
        currentContext = null
        lastMenuSig = null
        lastLevelSig = null
    }
}
