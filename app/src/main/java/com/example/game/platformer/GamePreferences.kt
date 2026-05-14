package com.example.game.platformer

import android.content.Context
import com.example.game.platformer.localization.AppLanguage
import kotlin.math.max
import kotlin.math.min

private const val PREFS_NAME = "platformer_prefs"
private const val KEY_TOTAL_COINS = "total_coins"
private const val KEY_SELECTED_SKIN_ID = "player_skin_selected_id"
private const val KEY_OWNED_SKIN_IDS = "player_skins_owned_csv"


private const val LEGACY_LEVEL_COUNT = 3
private const val KEY_UNLOCK_MIGRATION = "unlock_migration_expand_levels"
private const val KEY_AUDIO_ENABLED = "audio_enabled"
private const val KEY_APP_LANGUAGE = "app_language"

fun loadAppLanguage(context: Context): AppLanguage {
    val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    return AppLanguage.fromStorageCode(prefs.getString(KEY_APP_LANGUAGE, null))
}

fun saveAppLanguage(context: Context, language: AppLanguage) {
    context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        .edit()
        .putString(KEY_APP_LANGUAGE, language.storageCode)
        .apply()
}

fun loadAudioEnabled(context: Context): Boolean {
    val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    return prefs.getBoolean(KEY_AUDIO_ENABLED, true)
}

fun saveAudioEnabled(context: Context, enabled: Boolean) {
    context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        .edit()
        .putBoolean(KEY_AUDIO_ENABLED, enabled)
        .apply()
}


fun loadUnlockedLevels(context: Context, currentLevelCount: Int): Int {
    val maxLevel = currentLevelCount.coerceAtLeast(1)
    val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    var saved = prefs.getInt("unlocked_levels", 1).coerceAtLeast(1).coerceAtMost(maxLevel)

    if (!prefs.getBoolean(KEY_UNLOCK_MIGRATION, false)) {
        if (maxLevel > LEGACY_LEVEL_COUNT && saved >= LEGACY_LEVEL_COUNT) {
            saved = maxLevel
        }
        prefs.edit()
            .putInt("unlocked_levels", saved)
            .putBoolean(KEY_UNLOCK_MIGRATION, true)
            .apply()
    }

    return prefs.getInt("unlocked_levels", 1).coerceIn(1, maxLevel)
}

fun saveUnlockedLevels(context: Context, unlockedLevels: Int) {
    val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    prefs.edit().putInt("unlocked_levels", unlockedLevels).apply()
}

fun updateBestLevelRecord(context: Context, level: Int, result: LevelResult): BestRecordUpdate {
    val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    val timeKey = "best_time_cs_level_$level"
    val starsKey = "best_stars_level_$level"
    val existingBestTime = prefs.getInt(timeKey, Int.MAX_VALUE)
    val existingBestStars = prefs.getInt(starsKey, 0)

    val isFirstRecord = existingBestTime == Int.MAX_VALUE || existingBestStars == 0
    val isBetterTime = result.elapsedCentiseconds < existingBestTime
    val isBetterStars = result.stars > existingBestStars
    val isNewRecord = isFirstRecord || isBetterTime || isBetterStars

    val bestTime = min(existingBestTime, result.elapsedCentiseconds)
    val bestStars = max(existingBestStars, result.stars)

    prefs.edit()
        .putInt(timeKey, bestTime)
        .putInt(starsKey, bestStars)
        .apply()

    return BestRecordUpdate(
        record = BestLevelRecord(
            bestElapsedCentiseconds = if (bestTime == Int.MAX_VALUE) result.elapsedCentiseconds else bestTime,
            bestStars = bestStars
        ),
        isNewRecord = isNewRecord
    )
}

fun loadTotalCoins(context: Context): Int {
    val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    return prefs.getInt(KEY_TOTAL_COINS, 0).coerceAtLeast(0)
}


fun addTotalCoins(context: Context, delta: Int) {
    if (delta <= 0) return
    val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    val cur = prefs.getInt(KEY_TOTAL_COINS, 0).coerceAtLeast(0)
    prefs.edit().putInt(KEY_TOTAL_COINS, cur + delta).apply()
}


fun trySpendCoins(context: Context, amount: Int): Boolean {
    if (amount <= 0) return true
    val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    val cur = prefs.getInt(KEY_TOTAL_COINS, 0).coerceAtLeast(0)
    if (cur < amount) return false
    prefs.edit().putInt(KEY_TOTAL_COINS, cur - amount).apply()
    return true
}

fun loadOwnedSkinIds(context: Context): Set<Int> {
    val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    val raw = prefs.getString(KEY_OWNED_SKIN_IDS, "0") ?: "0"
    val parsed = raw.split(',').mapNotNull { it.trim().toIntOrNull() }.toMutableSet()
    if (!parsed.contains(0)) parsed.add(0)
    return parsed
}

fun saveOwnedSkinIds(context: Context, ids: Set<Int>) {
    val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    val withZero = ids.toMutableSet().apply { add(0) }
    prefs.edit().putString(KEY_OWNED_SKIN_IDS, withZero.sorted().joinToString(",")).apply()
}

fun loadSelectedSkinId(context: Context): Int {
    val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    val id = prefs.getInt(KEY_SELECTED_SKIN_ID, 0)
    val owned = loadOwnedSkinIds(context)
    return if (id in owned) id else 0
}

fun saveSelectedSkinId(context: Context, skinId: Int) {
    val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    prefs.edit().putInt(KEY_SELECTED_SKIN_ID, skinId).apply()
}

private const val KEY_STAT_LIFETIME_DEATHS = "stat_lifetime_deaths"
private const val KEY_STAT_LIFETIME_COINS_PICKED = "stat_lifetime_coins_picked"
private const val KEY_STAT_LEVELS_COMPLETED = "stat_levels_completed_total"
private const val KEY_STAT_PAID_SKINS_BOUGHT = "stat_paid_skins_bought"
private const val KEY_STAT_PLAY_TIME_MS = "stat_play_time_ms"
private const val KEY_ACHIEVEMENTS_UNLOCKED = "achievements_unlocked_csv"

fun incrementLifetimeDeaths(context: Context) {
    val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    val v = prefs.getInt(KEY_STAT_LIFETIME_DEATHS, 0) + 1
    prefs.edit().putInt(KEY_STAT_LIFETIME_DEATHS, v).apply()
}

fun incrementLifetimeCoinsPicked(context: Context, delta: Int) {
    if (delta <= 0) return
    val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    val v = prefs.getInt(KEY_STAT_LIFETIME_COINS_PICKED, 0) + delta
    prefs.edit().putInt(KEY_STAT_LIFETIME_COINS_PICKED, v).apply()
}

fun incrementLevelsCompletedTotal(context: Context) {
    val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    val v = prefs.getInt(KEY_STAT_LEVELS_COMPLETED, 0) + 1
    prefs.edit().putInt(KEY_STAT_LEVELS_COMPLETED, v).apply()
}

fun incrementPaidSkinsPurchased(context: Context) {
    val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    val v = prefs.getInt(KEY_STAT_PAID_SKINS_BOUGHT, 0) + 1
    prefs.edit().putInt(KEY_STAT_PAID_SKINS_BOUGHT, v).apply()
}

fun loadAchievementStats(context: Context): AchievementStats {
    val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    return AchievementStats(
        lifetimeDeaths = prefs.getInt(KEY_STAT_LIFETIME_DEATHS, 0).coerceAtLeast(0),
        lifetimeCoinsPicked = prefs.getInt(KEY_STAT_LIFETIME_COINS_PICKED, 0).coerceAtLeast(0),
        levelsCompletedTotal = prefs.getInt(KEY_STAT_LEVELS_COMPLETED, 0).coerceAtLeast(0),
        paidSkinsPurchased = prefs.getInt(KEY_STAT_PAID_SKINS_BOUGHT, 0).coerceAtLeast(0),
        skinsOwnedCount = loadOwnedSkinIds(context).size
    )
}

fun loadAchievementUnlockedIds(context: Context): MutableSet<String> {
    val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    val raw = prefs.getString(KEY_ACHIEVEMENTS_UNLOCKED, "") ?: ""
    if (raw.isBlank()) return mutableSetOf()
    return raw.split(',').map { it.trim() }.filter { it.isNotEmpty() }.toMutableSet()
}


fun unlockAchievementId(context: Context, id: String): Boolean {
    val set = loadAchievementUnlockedIds(context)
    if (id in set) return false
    set.add(id)
    val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    prefs.edit().putString(KEY_ACHIEVEMENTS_UNLOCKED, set.sorted().joinToString(",")).apply()
    return true
}

fun loadBestLevelRecord(context: Context, level: Int): BestLevelRecord? {
    val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    val timeKey = "best_time_cs_level_$level"
    val starsKey = "best_stars_level_$level"
    if (!prefs.contains(timeKey) || !prefs.contains(starsKey)) return null

    return BestLevelRecord(
        bestElapsedCentiseconds = prefs.getInt(timeKey, Int.MAX_VALUE),
        bestStars = prefs.getInt(starsKey, 0)
    )
}

fun updateBestTimeAttackLevelRecord(context: Context, slot: Int, result: LevelResult): BestRecordUpdate {
    val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    val timeKey = "best_time_cs_ta_level_$slot"
    val starsKey = "best_stars_ta_level_$slot"
    val existingBestTime = prefs.getInt(timeKey, Int.MAX_VALUE)
    val existingBestStars = prefs.getInt(starsKey, 0)

    val isFirstRecord = existingBestTime == Int.MAX_VALUE || existingBestStars == 0
    val isBetterTime = result.elapsedCentiseconds < existingBestTime
    val isBetterStars = result.stars > existingBestStars
    val isNewRecord = isFirstRecord || isBetterTime || isBetterStars

    val bestTime = min(existingBestTime, result.elapsedCentiseconds)
    val bestStars = max(existingBestStars, result.stars)

    prefs.edit()
        .putInt(timeKey, bestTime)
        .putInt(starsKey, bestStars)
        .apply()

    return BestRecordUpdate(
        record = BestLevelRecord(
            bestElapsedCentiseconds = if (bestTime == Int.MAX_VALUE) result.elapsedCentiseconds else bestTime,
            bestStars = bestStars
        ),
        isNewRecord = isNewRecord
    )
}

fun loadBestTimeAttackLevelRecord(context: Context, slot: Int): BestLevelRecord? {
    val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    val timeKey = "best_time_cs_ta_level_$slot"
    val starsKey = "best_stars_ta_level_$slot"
    if (!prefs.contains(timeKey) || !prefs.contains(starsKey)) return null

    return BestLevelRecord(
        bestElapsedCentiseconds = prefs.getInt(timeKey, Int.MAX_VALUE),
        bestStars = prefs.getInt(starsKey, 0)
    )
}

fun addPlayTimeMs(context: Context, deltaMs: Long) {
    if (deltaMs <= 0L) return
    val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    val cur = prefs.getLong(KEY_STAT_PLAY_TIME_MS, 0L).coerceAtLeast(0L)
    prefs.edit().putLong(KEY_STAT_PLAY_TIME_MS, cur + deltaMs).apply()
}

fun loadPlayTimeMs(context: Context): Long =
    context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        .getLong(KEY_STAT_PLAY_TIME_MS, 0L).coerceAtLeast(0L)

data class PlayerStatistics(
    val walletCoins: Int,
    val lifetimeDeaths: Int,
    val lifetimeCoinsPicked: Int,
    val levelsCompletedTotal: Int,
    val playTimeMs: Long,
    val unlockedCampaignLevels: Int,
    val campaignLevelCount: Int,
    val achievementsUnlocked: Int,
    val achievementsTotal: Int,
    val paidSkinsBought: Int,
    val skinsOwned: Int
)

fun loadPlayerStatistics(
    context: Context,
    campaignLevelCount: Int
): PlayerStatistics {
    val stats = loadAchievementStats(context)
    val unlocked = loadUnlockedLevels(context, campaignLevelCount)
    val achIds = loadAchievementUnlockedIds(context)
    val catalogSize = achievementCatalog(campaignLevelCount, AppLanguage.Russian).size
    return PlayerStatistics(
        walletCoins = loadTotalCoins(context),
        lifetimeDeaths = stats.lifetimeDeaths,
        lifetimeCoinsPicked = stats.lifetimeCoinsPicked,
        levelsCompletedTotal = stats.levelsCompletedTotal,
        playTimeMs = loadPlayTimeMs(context),
        unlockedCampaignLevels = unlocked,
        campaignLevelCount = campaignLevelCount,
        achievementsUnlocked = achIds.size,
        achievementsTotal = catalogSize,
        paidSkinsBought = stats.paidSkinsPurchased,
        skinsOwned = stats.skinsOwnedCount
    )
}
