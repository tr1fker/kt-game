package com.example.game.platformer.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.game.platformer.PlayerStatistics
import com.example.game.platformer.localization.LocalAppLanguage
import com.example.game.platformer.localization.formatPlayTimeMs
import com.example.game.platformer.localization.gameStrings

private val TextPrimary = Color(0xFF152028)
private val TextSecondary = Color(0xFF3A4A5C)

@Composable
fun StatisticsScreen(
    stats: PlayerStatistics,
    onBack: () -> Unit
) {
    val lang = LocalAppLanguage.current
    val s = remember(lang) { gameStrings(lang) }
    val rows = listOf(
        s.statWalletCoins to stats.walletCoins.toString(),
        s.statLifetimeDeaths to stats.lifetimeDeaths.toString(),
        s.statLifetimeCoins to stats.lifetimeCoinsPicked.toString(),
        s.statLevelsCompleted to stats.levelsCompletedTotal.toString(),
        s.statPlayTime to formatPlayTimeMs(stats.playTimeMs, lang),
        s.statUnlockedLevels to "${stats.unlockedCampaignLevels} / ${stats.campaignLevelCount}",
        s.statAchievements to "${stats.achievementsUnlocked} / ${stats.achievementsTotal}",
        s.statPaidSkins to stats.paidSkinsBought.toString(),
        s.statSkinsOwned to stats.skinsOwned.toString()
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
                text = s.statisticsTitle,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
        }
        Text(
            text = s.statisticsSubtitle,
            color = TextSecondary,
            modifier = Modifier.padding(top = 6.dp, bottom = 12.dp)
        )
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(rows, key = { it.first }) { (label, value) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = label,
                        color = TextPrimary,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = value,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}
