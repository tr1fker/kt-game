package com.example.game.platformer.audio

import android.content.Context
import android.media.MediaPlayer

fun MediaPlayer.setDataSourceForBgmTrack(app: Context, trackId: String) {
    val file = importedFileForPlayback(app, trackId)
    if (file != null) {
        setDataSource(file.absolutePath)
    } else {
        app.assets.openFd(trackId).use { afd ->
            setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
        }
    }
}
