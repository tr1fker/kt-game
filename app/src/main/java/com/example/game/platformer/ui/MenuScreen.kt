package com.example.game.platformer.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.foundation.layout.widthIn
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.game.platformer.BestLevelRecord
import com.example.game.platformer.Level
import com.example.game.platformer.audio.SoundEvent
import com.example.game.platformer.localization.LocalAppLanguage
import com.example.game.platformer.localization.formatCentisecondsLocalized
import com.example.game.platformer.localization.gameStrings

private val TaUiTitleDark = Color(0xFF2F353D)
private val TaUiTimerOnDark = Color(0xFFB8A666)


@Composable
fun MainMenuScreen(
    totalCoins: Int,
    audioEnabled: Boolean,
    onToggleAudio: () -> Unit,
    onLanguageToggle: () -> Unit,
    onPlay: () -> Unit,
    onOpenShop: () -> Unit,
    onOpenAchievements: () -> Unit,
    onOpenRecords: () -> Unit,
    onOpenStatistics: () -> Unit
) {
    val lang = LocalAppLanguage.current
    val s = remember(lang) { gameStrings(lang) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(28.dp)
    ) {
        LanguageToggleButton(
            onToggleLanguage = onLanguageToggle,
            modifier = Modifier.align(Alignment.BottomStart)
        )
        AudioToggleButton(
            audioEnabled = audioEnabled,
            onToggle = onToggleAudio,
            modifier = Modifier.align(Alignment.TopEnd)
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
        Text(
            text = s.mainTitle,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Text(
            text = s.mainSubtitle,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 8.dp, bottom = 20.dp),
            textAlign = TextAlign.Center
        )
        Text(
            text = buildAnnotatedString {
                append(s.coinsLabel)
                withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)) {
                    append(totalCoins.toString())
                }
            },
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 28.dp)
        )
        PlatformerPrimaryButton(
            onClick = onPlay,
            modifier = Modifier
                .fillMaxWidth(0.72f)
                .height(52.dp)
        ) {
            Text(s.play, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(14.dp))
        PlatformerSecondaryButton(
            onClick = onOpenShop,
            modifier = Modifier
                .fillMaxWidth(0.72f)
                .height(48.dp)
        ) {
            Text(s.shop, fontWeight = FontWeight.SemiBold)
        }
        Spacer(modifier = Modifier.height(14.dp))
        PlatformerSecondaryButton(
            onClick = onOpenAchievements,
            modifier = Modifier
                .fillMaxWidth(0.72f)
                .height(48.dp)
        ) {
            Text(s.achievements, fontWeight = FontWeight.SemiBold)
        }
        Spacer(modifier = Modifier.height(14.dp))
        PlatformerSecondaryButton(
            onClick = onOpenRecords,
            modifier = Modifier
                .fillMaxWidth(0.72f)
                .height(48.dp)
        ) {
            Text(s.records, fontWeight = FontWeight.SemiBold)
        }
        Spacer(modifier = Modifier.height(14.dp))
        PlatformerSecondaryButton(
            onClick = onOpenStatistics,
            modifier = Modifier
                .fillMaxWidth(0.72f)
                .height(48.dp)
        ) {
            Text(s.statistics, fontWeight = FontWeight.SemiBold)
        }
        }
    }
}


@Composable
fun LevelSelectScreen(
    levels: Int,
    unlockedLevels: Int,
    bestRecords: Map<Int, BestLevelRecord?>,
    onBack: () -> Unit,
    onSelectLevel: (Int) -> Unit,
    onOpenTimeAttack: () -> Unit
) {
    val lang = LocalAppLanguage.current
    val s = remember(lang) { gameStrings(lang) }
    val sounds = LocalGameSounds.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            ) {
                Text(
                    s.levelSelectTitle,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    s.levelSelectHint,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 10.dp)
                )
            }
            PlatformerTimeAttackChipButton(
                onClick = onOpenTimeAttack,
                modifier = Modifier
                    .padding(top = 2.dp)
                    .heightIn(min = 34.dp, max = 38.dp)
                    .widthIn(max = 102.dp)
            ) {
                Text(
                    s.timeAttackChip,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    fontSize = 11.sp
                )
            }
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(count = levels, key = { it }) { index ->
                val level = index + 1
                val isUnlocked = level <= unlockedLevels
                Button(
                    onClick = {
                        sounds?.play(SoundEvent.UI_CLICK)
                        if (isUnlocked) onSelectLevel(level)
                    },
                    enabled = isUnlocked,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(104.dp),
                    shape = PlatformerMenuButtonShape,
                    colors = PlatformerLevelGridButtonDefaults.campaignColors(isUnlocked),
                    contentPadding = PaddingValues(horizontal = 4.dp, vertical = 6.dp),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = if (isUnlocked) 3.dp else 0.dp,
                        pressedElevation = 1.dp,
                        disabledElevation = 0.dp
                    )
                ) {
                    val best = bestRecords[level]
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        if (best != null) {
                            val starsText = "★".repeat(best.bestStars) + "☆".repeat(3 - best.bestStars)
                            Text(
                                text = starsText,
                                style = MaterialTheme.typography.labelSmall,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        } else {
                            Spacer(modifier = Modifier.height(18.dp))
                        }
                        Text(
                            text = buildAnnotatedString {
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Medium)) {
                                    append(s.levelWord)
                                }
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append(level.toString())
                                }
                            },
                            style = MaterialTheme.typography.labelMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Visible,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 2.dp)
                        )
                        if (best != null) {
                            Text(
                                text = formatCentisecondsLocalized(best.bestElapsedCentiseconds, s.secondsShort),
                                style = MaterialTheme.typography.labelSmall,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 2.dp)
                            )
                        } else {
                            Spacer(modifier = Modifier.height(18.dp))
                        }
                    }
                }
            }
        }

        PlatformerBackOutlinedButton(
            onClick = onBack,
            text = s.backToMainMenu,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        )
    }
}


@Composable
fun TimeAttackLevelSelectScreen(
    timeAttackLevels: List<Level>,
    unlockedLevels: Int,
    bestRecords: Map<Int, BestLevelRecord?>,
    onBack: () -> Unit,
    onSelectLevel: (Int) -> Unit
) {
    val lang = LocalAppLanguage.current
    val s = remember(lang) { gameStrings(lang) }
    val sounds = LocalGameSounds.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            s.timeAttackScreenTitle,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = TaUiTitleDark
        )
        Text(
            s.timeAttackScreenSubtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 6.dp, bottom = 10.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(count = timeAttackLevels.size, key = { it }) { index ->
                val slot = index + 1
                val def = timeAttackLevels[index]
                val isUnlocked = slot <= unlockedLevels
                val limitSec = def.timeLimitSeconds
                Button(
                    onClick = {
                        sounds?.play(SoundEvent.UI_CLICK)
                        if (isUnlocked) onSelectLevel(slot)
                    },
                    enabled = isUnlocked,
                    shape = PlatformerMenuButtonShape,
                    colors = PlatformerLevelGridButtonDefaults.timeAttackColors(isUnlocked),
                    contentPadding = PaddingValues(horizontal = 4.dp, vertical = 6.dp),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = if (isUnlocked) 3.dp else 0.dp,
                        pressedElevation = 1.dp,
                        disabledElevation = 0.dp
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(104.dp)
                ) {
                    val best = bestRecords[slot]
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = if (limitSec != null) "⏱ $limitSec${s.secondsShort}" else s.timeLimitNone,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = TaUiTimerOnDark,
                            modifier = Modifier.padding(bottom = 2.dp)
                        )
                        if (best != null) {
                            val starsText = "★".repeat(best.bestStars) + "☆".repeat(3 - best.bestStars)
                            Text(
                                text = starsText,
                                style = MaterialTheme.typography.labelSmall,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        } else {
                            Spacer(modifier = Modifier.height(10.dp))
                        }
                        Text(
                            text = buildAnnotatedString {
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Medium)) {
                                    append(s.levelWord)
                                }
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append(slot.toString())
                                }
                            },
                            style = MaterialTheme.typography.labelMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Visible,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 2.dp)
                        )
                        if (best != null) {
                            Text(
                                text = formatCentisecondsLocalized(best.bestElapsedCentiseconds, s.secondsShort),
                                style = MaterialTheme.typography.labelSmall,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 2.dp)
                            )
                        } else {
                            Spacer(modifier = Modifier.height(10.dp))
                        }
                    }
                }
            }
        }

        PlatformerBackOutlinedButton(
            onClick = onBack,
            text = s.backToLevelSelect,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        )
    }
}
