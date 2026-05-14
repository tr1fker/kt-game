package com.example.game.platformer.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.game.platformer.BestLevelRecord
import com.example.game.platformer.localization.AppLanguage
import com.example.game.platformer.localization.LocalAppLanguage
import com.example.game.platformer.localization.formatCentisecondsLocalized
import com.example.game.platformer.localization.gameStrings

private val TextPrimary = Color(0xFF152028)
private val TextSecondary = Color(0xFF3A4A5C)

@Composable
fun RecordsScreen(
    levels: Int,
    bestRecords: Map<Int, BestLevelRecord?>,
    onBack: () -> Unit
) {
    val lang = LocalAppLanguage.current
    val s = remember(lang) { gameStrings(lang) }
    val sortedLevels = (1..levels).sortedWith(
        compareByDescending<Int> { bestRecords[it]?.bestStars ?: -1 }
            .thenBy { bestRecords[it]?.bestElapsedCentiseconds ?: Int.MAX_VALUE }
            .thenBy { it }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            PlatformerBackOutlinedButton(
                onClick = onBack,
                text = s.back,
                compact = true
            )
            Text(
                text = s.recordsTitle,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
        }
        Text(
            text = s.recordsSortHint,
            color = TextSecondary,
            modifier = Modifier.padding(top = 6.dp, bottom = 10.dp)
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            itemsIndexed(sortedLevels, key = { _, level -> level }) { rank, level ->
                val best = bestRecords[level]
                val starsText = if (best == null) "---" else "★".repeat(best.bestStars) + "☆".repeat(3 - best.bestStars)
                val timeText = if (best == null) "---" else formatCentisecondsLocalized(best.bestElapsedCentiseconds, s.secondsShort)
                val rankLabel = when (rank) {
                    0 -> s.place1
                    1 -> s.place2
                    2 -> s.place3
                    else -> when (lang) {
                        AppLanguage.Russian -> "${rank + 1}${s.placeSuffix}"
                        AppLanguage.English -> "Place ${rank + 1}"
                    }
                }
                val medalLabel = when (rank) {
                    0 -> s.medalGold
                    1 -> s.medalSilver
                    2 -> s.medalBronze
                    else -> ""
                }
                val rowBackground = when (rank) {
                    0 -> Color(0xFFFFF4D2)
                    1 -> Color(0xFFEEF4FF)
                    2 -> Color(0xFFFFE8DC)
                    else -> Color(0xFFF2F4F8)
                }
                val borderColor = when (rank) {
                    0 -> Color(0xFFE0B84A)
                    1 -> Color(0xFF8FA8D6)
                    2 -> Color(0xFFD9956E)
                    else -> Color(0xFFB8C0CC)
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(rowBackground, RoundedCornerShape(10.dp))
                        .border(1.5.dp, borderColor, RoundedCornerShape(10.dp))
                        .padding(horizontal = 14.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = if (medalLabel.isNotEmpty()) "${s.levelWord}$level $medalLabel" else "${s.levelWord}$level",
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary
                        )
                        Text(rankLabel, color = TextSecondary)
                    }
                    Text(
                        text = "$starsText   $timeText",
                        color = TextPrimary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}
