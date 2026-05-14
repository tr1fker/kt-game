package com.example.game.platformer

import com.example.game.platformer.localization.GameStrings
import com.example.game.platformer.localization.formatCentisecondsLocalized

fun calculateStars(elapsedCentiseconds: Int, deaths: Int): Int {
    return when {
        deaths == 0 && elapsedCentiseconds <= 3000 -> 3
        deaths <= 2 && elapsedCentiseconds <= 5500 -> 2
        else -> 1
    }
}

fun formatCentiseconds(totalCentiseconds: Int): String {
    val safe = totalCentiseconds.coerceAtLeast(0)
    val secondsPart = safe / 100
    val centisecondsPart = safe % 100
    return String.format("%d.%02d s", secondsPart, centisecondsPart)
}

fun buildResultSubtitle(result: LevelResult?, s: GameStrings): String {
    if (result == null) return s.resultDataUnavailable
    val starsText = "★".repeat(result.stars) + "☆".repeat(3 - result.stars)
    val bestStarsText = "★".repeat(result.bestStars) + "☆".repeat(3 - result.bestStars)
    val newBadge = if (result.isNewRecord) s.newRecordSuffix else ""
    val runTime = formatCentisecondsLocalized(result.elapsedCentiseconds, s.secondsShort)
    val bestTime = formatCentisecondsLocalized(result.bestElapsedCentiseconds, s.secondsShort)
    val coinsLine =
        if (result.coinsCollectedThisRun > 0) {
            "${s.resultCoinsLine}${result.coinsCollectedThisRun}"
        } else {
            ""
        }
    return s.resultRun + runTime +
        s.resultDeaths + result.deaths +
        s.resultStars + starsText + newBadge +
        s.resultBest + bestTime +
        s.resultStars + bestStarsText +
        coinsLine
}
