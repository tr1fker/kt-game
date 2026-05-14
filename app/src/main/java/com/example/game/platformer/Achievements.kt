package com.example.game.platformer

import android.content.Context
import com.example.game.platformer.localization.AppLanguage

data class AchievementEvalContext(
    val totalLevelsInGame: Int,
    val skinCatalogSize: Int
)

data class AchievementDef(
    val id: String,
    val title: String,
    val description: String,
    val isUnlocked: (AchievementStats, AchievementEvalContext) -> Boolean
)

data class AchievementStats(
    val lifetimeDeaths: Int,
    val lifetimeCoinsPicked: Int,
    val levelsCompletedTotal: Int,
    val paidSkinsPurchased: Int,
    val skinsOwnedCount: Int
)

private fun milestones(
    prefix: String,
    thresholds: List<Int>,
    lang: AppLanguage,
    predicate: (AchievementStats, Int) -> Boolean
): List<AchievementDef> =
    thresholds.map { t ->
        AchievementDef(
            id = "${prefix}_$t",
            title = when (prefix) {
                "death" -> when (lang) {
                    AppLanguage.Russian -> "Смертей всего: $t"
                    AppLanguage.English -> "Total deaths: $t"
                }
                "coin" -> when (lang) {
                    AppLanguage.Russian -> "Монет собрано всего: $t"
                    AppLanguage.English -> "Total coins collected: $t"
                }
                "level" -> when (lang) {
                    AppLanguage.Russian -> "Уровней пройдено: $t"
                    AppLanguage.English -> "Levels cleared: $t"
                }
                else -> "$t"
            },
            description = when (prefix) {
                "death" -> when (lang) {
                    AppLanguage.Russian -> "Накопить $t смертей за всё время"
                    AppLanguage.English -> "Reach $t deaths in total"
                }
                "coin" -> when (lang) {
                    AppLanguage.Russian -> "Подобрать $t монет за всё время"
                    AppLanguage.English -> "Collect $t coins in total"
                }
                "level" -> when (lang) {
                    AppLanguage.Russian -> "Успешно завершить уровень $t раз (суммарно)"
                    AppLanguage.English -> "Clear any level $t times in total"
                }
                else -> ""
            },
            isUnlocked = { s, _ -> predicate(s, t) }
        )
    }

fun achievementCatalog(totalLevelsInGame: Int, lang: AppLanguage): List<AchievementDef> = buildList {
    val deathT = listOf(1, 5, 10, 20, 50, 100, 200, 500, 1000)
    val coinT = listOf(1, 5, 10, 20, 50, 100, 200, 500, 1000)
    val levelT = listOf(1, 3, 5, 10, 15, 18, 19, 20, 25, 30, 50, 100)
    addAll(milestones("death", deathT, lang) { s, t -> s.lifetimeDeaths >= t })
    addAll(milestones("coin", coinT, lang) { s, t -> s.lifetimeCoinsPicked >= t })
    addAll(milestones("level", levelT, lang) { s, t -> s.levelsCompletedTotal >= t })
    add(
        AchievementDef(
            id = "skin_first_paid",
            title = when (lang) {
                AppLanguage.Russian -> "Модник"
                AppLanguage.English -> "Fashionista"
            },
            description = when (lang) {
                AppLanguage.Russian -> "Купить первый платный скин"
                AppLanguage.English -> "Buy your first paid skin"
            },
            isUnlocked = { s, _ -> s.paidSkinsPurchased >= 1 }
        )
    )
    add(
        AchievementDef(
            id = "skin_all",
            title = when (lang) {
                AppLanguage.Russian -> "Полная коллекция"
                AppLanguage.English -> "Full collection"
            },
            description = when (lang) {
                AppLanguage.Russian -> "Открыть все скины в магазине"
                AppLanguage.English -> "Unlock every skin in the shop"
            },
            isUnlocked = { s, ctx -> s.skinsOwnedCount >= ctx.skinCatalogSize && ctx.skinCatalogSize > 0 }
        )
    )
    add(
        AchievementDef(
            id = "beat_all_levels_once",
            title = when (lang) {
                AppLanguage.Russian -> "Мастер платформ"
                AppLanguage.English -> "Platform master"
            },
            description = when (lang) {
                AppLanguage.Russian -> "Завершить уровни не меньше раз, чем уровней в игре ($totalLevelsInGame)"
                AppLanguage.English -> "Clear levels at least as many times as there are levels ($totalLevelsInGame)"
            },
            isUnlocked = { s, ctx -> s.levelsCompletedTotal >= ctx.totalLevelsInGame && ctx.totalLevelsInGame > 0 }
        )
    )
}

fun evaluateAchievements(
    context: Context,
    totalLevelsInGame: Int,
    lang: AppLanguage,
    overrideSkinsOwned: Int? = null
): List<String> {
    val base = loadAchievementStats(context)
    val stats = if (overrideSkinsOwned != null) {
        base.copy(skinsOwnedCount = overrideSkinsOwned)
    } else {
        base
    }
    val skinCatalogSize = playerSkinCatalog().size
    val ctx = AchievementEvalContext(
        totalLevelsInGame = totalLevelsInGame,
        skinCatalogSize = skinCatalogSize
    )
    val unlocked = loadAchievementUnlockedIds(context).toMutableSet()
    val catalog = achievementCatalog(totalLevelsInGame, lang)
    val newTitles = mutableListOf<String>()
    for (a in catalog) {
        if (a.id in unlocked) continue
        if (a.isUnlocked(stats, ctx)) {
            if (unlockAchievementId(context, a.id)) {
                unlocked.add(a.id)
                newTitles.add(a.title)
            }
        }
    }
    return newTitles
}

object AchievementEvents {
    fun onPlayerDeath(context: Context, totalLevelsInGame: Int, lang: AppLanguage): List<String> {
        incrementLifetimeDeaths(context)
        return evaluateAchievements(context, totalLevelsInGame, lang)
    }

    fun onCoinPicked(context: Context, totalLevelsInGame: Int, lang: AppLanguage): List<String> {
        incrementLifetimeCoinsPicked(context, 1)
        return evaluateAchievements(context, totalLevelsInGame, lang)
    }

    fun onLevelBeaten(context: Context, totalLevelsInGame: Int, lang: AppLanguage): List<String> {
        incrementLevelsCompletedTotal(context)
        return evaluateAchievements(context, totalLevelsInGame, lang)
    }

    fun onSkinBought(
        context: Context,
        totalLevelsInGame: Int,
        wasPaidPurchase: Boolean,
        ownedCount: Int,
        lang: AppLanguage
    ): List<String> {
        if (wasPaidPurchase) {
            incrementPaidSkinsPurchased(context)
        }
        return evaluateAchievements(context, totalLevelsInGame, lang, overrideSkinsOwned = ownedCount)
    }

    fun refreshWithSkinCount(context: Context, totalLevelsInGame: Int, ownedCount: Int, lang: AppLanguage): List<String> =
        evaluateAchievements(context, totalLevelsInGame, lang, overrideSkinsOwned = ownedCount)
}
