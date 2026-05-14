package com.example.game.platformer.audio

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import java.io.File
import java.io.FileOutputStream

const val BGM_IMPORTED_PREFIX = "imported:"

private val IMPORTABLE_EXT = listOf(".ogg", ".mp3", ".wav", ".m4a", ".aac", ".flac")

private fun importedRoot(ctx: Context): File =
    File(ctx.filesDir, "music_imports").also { it.mkdirs() }

fun listImportedBgmTrackIds(context: Context): List<String> {
    val dir = importedRoot(context)
    if (!dir.isDirectory) return emptyList()
    return dir.listFiles()
        ?.asSequence()
        ?.filter { it.isFile }
        ?.filter { f ->
            val lower = f.name.lowercase()
            IMPORTABLE_EXT.any { lower.endsWith(it) }
        }
        ?.map { BGM_IMPORTED_PREFIX + it.name }
        ?.sorted()
        ?.toList()
        .orEmpty()
}

fun importedFileForPlayback(context: Context, trackId: String): File? {
    if (!trackId.startsWith(BGM_IMPORTED_PREFIX)) return null
    val name = trackId.removePrefix(BGM_IMPORTED_PREFIX)
    if (name.isEmpty()) return null
    if (name.contains(File.separatorChar) || name.contains('/') || name.contains('\\')) return null
    if (name == "." || name == "..") return null
    val dir = importedRoot(context)
    val dirCanon = try {
        dir.canonicalFile
    } catch (_: Exception) {
        return null
    }
    val f = File(dir, name)
    val parent = try {
        f.parentFile?.canonicalFile
    } catch (_: Exception) {
        return null
    } ?: return null
    if (parent != dirCanon) return null
    return if (f.isFile) f else null
}

fun deleteImportedBgmTrack(context: Context, trackId: String): Boolean {
    val f = importedFileForPlayback(context, trackId) ?: return false
    return f.delete()
}

private fun sanitizeBaseName(raw: String): String {
    val trimmed = raw.trim().ifBlank { "track" }.take(80)
    return trimmed.replace(Regex("[^a-zA-Z0-9._\\-]"), "_").ifBlank { "track" }
}

private fun displayNameFromUri(context: Context, uri: Uri): String? {
    return context.contentResolver.query(
        uri,
        arrayOf(OpenableColumns.DISPLAY_NAME),
        null,
        null,
        null,
    )?.use { c ->
        if (c.moveToFirst()) {
            val i = c.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (i >= 0) c.getString(i) else null
        } else {
            null
        }
    }
}

private fun extFromName(name: String?): String {
    val n = name?.lowercase().orEmpty()
    val known = listOf(".ogg", ".mp3", ".wav", ".m4a", ".aac", ".flac")
    for (e in known) {
        if (n.endsWith(e)) return e.removePrefix(".")
    }
    val last = n.substringAfterLast('.', "")
    return if (last.length in 2..5) last else "ogg"
}

/** Копирует выбранный аудиофайл во внутреннее хранилище приложения. Лучше вызывать не с главного потока. */
fun importBgmFromPickerUri(context: Context, uri: Uri): Result<String> {
    return runCatching {
        val cr = context.contentResolver
        val display = displayNameFromUri(context, uri)
        val ext = extFromName(display)
        val base = sanitizeBaseName(display?.substringBeforeLast('.', "") ?: "track")
        val fileName = "${System.currentTimeMillis()}_$base.$ext"
        val outFile = File(importedRoot(context), fileName)
        cr.openInputStream(uri)?.use { input ->
            FileOutputStream(outFile).use { out -> input.copyTo(out) }
        } ?: error("openInputStream")
        BGM_IMPORTED_PREFIX + fileName
    }
}
