package com.example.game.platformer.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper

/**
 * Предпросмотр трека из встроенных assets или из импорта с устройства.
 * На время воспроизведения ставит на паузу основной [GameMusicPlayer].
 */
class MusicTrackPreviewPlayer(
    private val app: Context,
    private val onPauseMainBgm: () -> Unit,
    private val onResumeMainBgm: () -> Unit,
    private val onActivePreviewPathChanged: (String?) -> Unit = { _ -> },
) {
    private val mainHandler = Handler(Looper.getMainLooper())
    private var player: MediaPlayer? = null
    private var currentPath: String? = null
    private var mainBgmPausedByUs: Boolean = false

    fun currentPreviewPath(): String? = currentPath

    /**
     * Запускает [path] или останавливает, если он уже играет.
     * @return true, если после вызова идёт воспроизведение [path].
     */
    fun toggle(path: String): Boolean {
        if (currentPath == path && player?.isPlaying == true) {
            stop()
            return false
        }
        val wasPreviewing = player != null
        releasePlayerOnly()
        currentPath = null
        onActivePreviewPathChanged(null)
        if (!wasPreviewing && !mainBgmPausedByUs) {
            onPauseMainBgm()
            mainBgmPausedByUs = true
        }
        return try {
            val p = MediaPlayer()
            p.setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
            )
            p.setDataSourceForBgmTrack(app, path)
            p.setOnCompletionListener { completed ->
                if (completed != p) return@setOnCompletionListener
                mainHandler.post {
                    if (player === p) {
                        releasePlayerOnly()
                        currentPath = null
                        onActivePreviewPathChanged(null)
                        resumeMainBgmIfWePaused()
                    }
                }
            }
            p.prepare()
            p.start()
            player = p
            currentPath = path
            onActivePreviewPathChanged(path)
            true
        } catch (_: Exception) {
            releasePlayerOnly()
            currentPath = null
            onActivePreviewPathChanged(null)
            resumeMainBgmIfWePaused()
            false
        }
    }

    fun stop() {
        releasePlayerOnly()
        currentPath = null
        onActivePreviewPathChanged(null)
        resumeMainBgmIfWePaused()
    }

    fun release() {
        stop()
    }

    private fun releasePlayerOnly() {
        runCatching {
            player?.setOnCompletionListener(null)
            player?.stop()
            player?.release()
        }
        player = null
    }

    private fun resumeMainBgmIfWePaused() {
        if (!mainBgmPausedByUs) return
        mainBgmPausedByUs = false
        onResumeMainBgm()
    }
}
