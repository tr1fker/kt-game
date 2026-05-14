package com.example.game.platformer.audio

import android.content.Context

private const val PREFS = "platformer_prefs"
private const val KEY_MENU_BGM = "bgm_menu_paths_csv"
private const val KEY_LEVEL_BGM = "bgm_level_paths_csv"

private const val DEFAULT_MENU = "music/bgm_menu.ogg"
private const val DEFAULT_LEVEL = "music/bgm_level.ogg"

private val AUDIO_EXT = listOf(".ogg", ".mp3", ".wav")

fun listBundledMusicAssetPaths(context: Context): List<String> {
    val names = try {
        context.assets.list("music")?.toList().orEmpty()
    } catch (_: Exception) {
        emptyList()
    }
    return names
        .asSequence()
        .filter { name ->
            val lower = name.lowercase()
            if (lower.endsWith(".txt")) return@filter false
            AUDIO_EXT.any { lower.endsWith(it) }
        }
        .map { "music/$it" }
        .sorted()
        .toList()
}

private fun parseCsv(raw: String?): List<String> =
    raw?.split(',')
        ?.map { it.trim() }
        ?.filter { it.isNotEmpty() }
        .orEmpty()

fun allBgmTrackIds(context: Context): List<String> =
    (listBundledMusicAssetPaths(context) + listImportedBgmTrackIds(context)).distinct().sorted()

private fun normalizeMenuSelection(selected: List<String>, available: Set<String>): List<String> {
    val valid = selected.filter { it in available }.distinct()
    if (valid.isNotEmpty()) return valid
    if (DEFAULT_MENU in available) return listOf(DEFAULT_MENU)
    return available.sorted().take(1)
}

private fun normalizeLevelSelection(selected: List<String>, available: Set<String>): List<String> {
    val valid = selected.filter { it in available }.distinct()
    if (valid.isNotEmpty()) return valid
    if (DEFAULT_LEVEL in available) return listOf(DEFAULT_LEVEL)
    return available.sorted().take(1)
}

fun loadMenuBgmPlaylist(context: Context): List<String> {
    val available = allBgmTrackIds(context).toSet()
    if (available.isEmpty()) return emptyList()
    val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
    val saved = parseCsv(prefs.getString(KEY_MENU_BGM, null))
    return normalizeMenuSelection(saved, available)
}

fun loadLevelBgmPlaylist(context: Context): List<String> {
    val available = allBgmTrackIds(context).toSet()
    if (available.isEmpty()) return emptyList()
    val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
    val saved = parseCsv(prefs.getString(KEY_LEVEL_BGM, null))
    return normalizeLevelSelection(saved, available)
}

fun saveMenuBgmPlaylist(context: Context, paths: List<String>) {
    context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        .edit()
        .putString(KEY_MENU_BGM, paths.joinToString(","))
        .apply()
}

fun saveLevelBgmPlaylist(context: Context, paths: List<String>) {
    context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        .edit()
        .putString(KEY_LEVEL_BGM, paths.joinToString(","))
        .apply()
}

fun coerceMenuPlaylist(context: Context, selected: Collection<String>): List<String> {
    val available = allBgmTrackIds(context).toSet()
    if (available.isEmpty()) return emptyList()
    return normalizeMenuSelection(selected.toList(), available)
}

fun coerceLevelPlaylist(context: Context, selected: Collection<String>): List<String> {
    val available = allBgmTrackIds(context).toSet()
    if (available.isEmpty()) return emptyList()
    return normalizeLevelSelection(selected.toList(), available)
}

fun displayTrackTitle(trackId: String): String {
    val raw = if (trackId.startsWith(BGM_IMPORTED_PREFIX)) {
        trackId.removePrefix(BGM_IMPORTED_PREFIX).substringBeforeLast('.')
    } else {
        trackId.removePrefix("music/").substringBeforeLast('.')
    }
    val withoutLeadingId = raw.replaceFirst(Regex("^\\d+_"), "")
    return withoutLeadingId.replace('_', ' ').trim().ifBlank { trackId }
}
