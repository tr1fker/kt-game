package com.example.game.platformer.ui

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.game.platformer.AchievementEvents
import com.example.game.platformer.LevelResult
import com.example.game.platformer.PlatformerDestination
import com.example.game.platformer.addTotalCoins
import com.example.game.platformer.buildResultSubtitle
import com.example.game.platformer.loadAppLanguage
import com.example.game.platformer.loadAudioEnabled
import com.example.game.platformer.loadBestLevelRecord
import com.example.game.platformer.loadBestTimeAttackLevelRecord
import com.example.game.platformer.loadOwnedSkinIds
import com.example.game.platformer.loadSelectedSkinId
import com.example.game.platformer.loadPlayerStatistics
import com.example.game.platformer.loadTotalCoins
import com.example.game.platformer.loadUnlockedLevels
import com.example.game.platformer.levels.allLevels
import com.example.game.platformer.levels.timeattack.allTimeAttackLevels
import com.example.game.platformer.saveAppLanguage
import com.example.game.platformer.saveAudioEnabled
import com.example.game.platformer.saveOwnedSkinIds
import com.example.game.platformer.saveSelectedSkinId
import com.example.game.platformer.saveUnlockedLevels
import com.example.game.platformer.skinById
import com.example.game.platformer.trySpendCoins
import com.example.game.platformer.updateBestLevelRecord
import com.example.game.platformer.updateBestTimeAttackLevelRecord
import com.example.game.platformer.audio.GameMusicPlayer
import com.example.game.platformer.audio.GameSoundPlayer
import com.example.game.platformer.audio.SoundEvent
import com.example.game.platformer.localization.AppLanguage
import com.example.game.platformer.localization.LocalAppLanguage
import com.example.game.platformer.localization.gameStrings
import com.example.game.ui.theme.GameTheme
import kotlinx.coroutines.delay


private const val ALL_LEVELS_UNLOCKED_FOR_TEST = true

/** Экраны, на которых должен играть плейлист «уровня», а не меню (в т.ч. окно результата между уровнями). */
private fun shouldPlayLevelBgm(screen: PlatformerDestination): Boolean =
    when (screen) {
        PlatformerDestination.Playing,
        PlatformerDestination.LevelComplete,
        PlatformerDestination.GameComplete -> true
        else -> false
    }

@Composable
fun PlatformerApp() {
    val context = LocalContext.current
    val appCtx = context.applicationContext
    val levels = remember { allLevels() }
    val timeAttackLevels = remember { allTimeAttackLevels() }
    var playingTimeAttack by remember { mutableStateOf(false) }
    var timeAttackRestartKey by remember { mutableIntStateOf(0) }
    var unlockedLevels by remember {
        mutableIntStateOf(
            if (ALL_LEVELS_UNLOCKED_FOR_TEST) levels.size
            else loadUnlockedLevels(context, levels.size)
        )
    }
    var totalCoins by remember { mutableIntStateOf(loadTotalCoins(context)) }
    var selectedSkinId by remember { mutableIntStateOf(loadSelectedSkinId(context)) }
    var ownedSkinIds by remember { mutableStateOf(loadOwnedSkinIds(context)) }
    val activeSkin = remember(selectedSkinId) { skinById(selectedSkinId) }
    var currentLevel by remember { mutableIntStateOf(1) }
    var screen by remember { mutableStateOf(PlatformerDestination.MainMenu) }
    var lastLevelResult by remember { mutableStateOf<LevelResult?>(null) }
    var achievementBanner by remember { mutableStateOf<String?>(null) }
    var audioEnabled by remember { mutableStateOf(loadAudioEnabled(appCtx)) }
    var appLanguage by remember { mutableStateOf(loadAppLanguage(appCtx)) }
    var musicPrefsEpoch by remember { mutableIntStateOf(0) }
    val uiText = gameStrings(appLanguage)

    val soundPlayer = remember(appCtx) { GameSoundPlayer(appCtx) }
    DisposableEffect(soundPlayer) {
        onDispose { soundPlayer.close() }
    }

    val musicPlayer = remember(appCtx) { GameMusicPlayer(appCtx) }
    DisposableEffect(musicPlayer) {
        onDispose { musicPlayer.close() }
    }

    LaunchedEffect(screen, audioEnabled) {
        soundPlayer.setSoundEnabled(audioEnabled)
        musicPlayer.setMusicEnabled(audioEnabled)
        if (!audioEnabled) return@LaunchedEffect
        if (shouldPlayLevelBgm(screen)) {
            musicPlayer.playLevelIfNeeded()
        } else {
            musicPlayer.playMenuIfNeeded()
        }
    }

    LaunchedEffect(musicPrefsEpoch) {
        if (musicPrefsEpoch == 0) return@LaunchedEffect
        if (!audioEnabled) return@LaunchedEffect
        musicPlayer.clearBgmStateCache()
        if (shouldPlayLevelBgm(screen)) {
            musicPlayer.playLevelIfNeeded()
        } else {
            musicPlayer.playMenuIfNeeded()
        }
    }

    LaunchedEffect(achievementBanner) {
        if (achievementBanner != null) {
            delay(3200)
            achievementBanner = null
        }
    }

    fun pushAchievementToasts(titles: List<String>) {
        if (titles.isEmpty()) return
        soundPlayer.play(SoundEvent.ACHIEVEMENT)
        val extra = titles.size - 2
        achievementBanner = buildString {
            append("🏆 ")
            append(titles.take(2).joinToString(" · "))
            if (extra > 0) append(" +").append(extra)
        }
    }

    fun toggleAudio() {
        val next = !audioEnabled
        audioEnabled = next
        saveAudioEnabled(context, next)
    }

    CompositionLocalProvider(LocalGameSounds provides soundPlayer) {
    CompositionLocalProvider(LocalAppLanguage provides appLanguage) {
    Box(modifier = Modifier.fillMaxSize()) {
        Crossfade(
            targetState = screen,
            modifier = Modifier.fillMaxSize(),
            animationSpec = tween(durationMillis = 320),
            label = "platformerNav"
        ) { dest ->
            when (dest) {
            PlatformerDestination.MainMenu -> {
                MainMenuScreen(
                    totalCoins = totalCoins,
                    audioEnabled = audioEnabled,
                    onToggleAudio = ::toggleAudio,
                    onLanguageToggle = {
                        appLanguage = appLanguage.toggled()
                        saveAppLanguage(context, appLanguage)
                    },
                    onPlay = { screen = PlatformerDestination.LevelSelect },
                    onOpenShop = {
                        totalCoins = loadTotalCoins(context)
                        ownedSkinIds = loadOwnedSkinIds(context)
                        selectedSkinId = loadSelectedSkinId(context)
                        screen = PlatformerDestination.Shop
                    },
                    onOpenMusic = { screen = PlatformerDestination.MusicSettings },
                    onOpenAchievements = { screen = PlatformerDestination.Achievements },
                    onOpenRecords = { screen = PlatformerDestination.Records },
                    onOpenStatistics = { screen = PlatformerDestination.Statistics }
                )
            }

            PlatformerDestination.LevelSelect -> {
                LevelSelectScreen(
                    levels = levels.size,
                    unlockedLevels = unlockedLevels,
                    bestRecords = (1..levels.size).associateWith { level ->
                        loadBestLevelRecord(context, level)
                    },
                    onBack = { screen = PlatformerDestination.MainMenu },
                    onSelectLevel = { selectedLevel ->
                        playingTimeAttack = false
                        currentLevel = selectedLevel
                        screen = PlatformerDestination.Playing
                    },
                    onOpenTimeAttack = { screen = PlatformerDestination.TimeAttackLevelSelect }
                )
            }

            PlatformerDestination.TimeAttackLevelSelect -> {
                TimeAttackLevelSelectScreen(
                    timeAttackLevels = timeAttackLevels,
                    unlockedLevels = unlockedLevels.coerceAtMost(timeAttackLevels.size),
                    bestRecords = (1..timeAttackLevels.size).associateWith { slot ->
                        loadBestTimeAttackLevelRecord(context, slot)
                    },
                    onBack = { screen = PlatformerDestination.LevelSelect },
                    onSelectLevel = { selectedLevel ->
                        playingTimeAttack = true
                        currentLevel = selectedLevel
                        screen = PlatformerDestination.Playing
                    }
                )
            }

            PlatformerDestination.Shop -> {
                ShopScreen(
                    totalCoins = totalCoins,
                    ownedSkinIds = ownedSkinIds,
                    selectedSkinId = selectedSkinId,
                    onBuySkin = { skin ->
                        if (skin.id in ownedSkinIds) {
                            true
                        } else if (!trySpendCoins(context, skin.priceCoins)) {
                            false
                        } else {
                            val wasPaid = skin.priceCoins > 0
                            val next = ownedSkinIds + skin.id
                            saveOwnedSkinIds(context, next)
                            ownedSkinIds = next
                            totalCoins = loadTotalCoins(context)
                            pushAchievementToasts(
                                AchievementEvents.onSkinBought(
                                    appCtx,
                                    levels.size,
                                    wasPaid,
                                    next.size,
                                    appLanguage
                                )
                            )
                            true
                        }
                    },
                    onEquipSkin = { id: Int ->
                        if (id in ownedSkinIds) {
                            saveSelectedSkinId(context, id)
                            selectedSkinId = id
                        }
                    },
                    onBack = { screen = PlatformerDestination.MainMenu }
                )
            }

            PlatformerDestination.Achievements -> {
                AchievementsScreen(
                    totalLevelsInGame = levels.size,
                    onBack = { screen = PlatformerDestination.MainMenu }
                )
            }

            PlatformerDestination.Records -> {
                RecordsScreen(
                    levels = levels.size,
                    bestRecords = (1..levels.size).associateWith { level ->
                        loadBestLevelRecord(context, level)
                    },
                    onBack = { screen = PlatformerDestination.MainMenu }
                )
            }

            PlatformerDestination.Statistics -> {
                StatisticsScreen(
                    stats = loadPlayerStatistics(context, levels.size),
                    onBack = { screen = PlatformerDestination.MainMenu }
                )
            }

            PlatformerDestination.MusicSettings -> {
                MusicSettingsScreen(
                    onBack = { screen = PlatformerDestination.MainMenu },
                    onSaved = { musicPrefsEpoch += 1 },
                    musicPlayer = musicPlayer,
                )
            }

            PlatformerDestination.Playing -> {
                key(
                    if (playingTimeAttack) "ta-${currentLevel}-${timeAttackRestartKey}"
                    else "main-$currentLevel"
                ) {
                    GameScreen(
                        levelIndex = currentLevel,
                        level = if (playingTimeAttack) {
                            timeAttackLevels[currentLevel - 1]
                        } else {
                            levels[currentLevel - 1]
                        },
                        onBack = {
                            screen = if (playingTimeAttack) {
                                PlatformerDestination.TimeAttackLevelSelect
                            } else {
                                PlatformerDestination.LevelSelect
                            }
                        },
                        onLevelCompleted = { result ->
                            val updatedBest = if (playingTimeAttack) {
                                updateBestTimeAttackLevelRecord(context, currentLevel, result)
                            } else {
                                updateBestLevelRecord(context, currentLevel, result)
                            }
                            addTotalCoins(context, result.coinsCollectedThisRun)
                            totalCoins = loadTotalCoins(context)
                            if (!playingTimeAttack) {
                                pushAchievementToasts(AchievementEvents.onLevelBeaten(appCtx, levels.size, appLanguage))
                                if (currentLevel == unlockedLevels && unlockedLevels < levels.size) {
                                    unlockedLevels += 1
                                    saveUnlockedLevels(context, unlockedLevels)
                                }
                            }
                            lastLevelResult = result.copy(
                                bestElapsedCentiseconds = updatedBest.record.bestElapsedCentiseconds,
                                bestStars = updatedBest.record.bestStars,
                                isNewRecord = updatedBest.isNewRecord
                            )
                            screen = when {
                                playingTimeAttack -> PlatformerDestination.LevelComplete
                                currentLevel < levels.size -> PlatformerDestination.LevelComplete
                                else -> PlatformerDestination.GameComplete
                            }
                        },
                        playerMainColor = activeSkin.mainColor,
                        playerAccentColor = activeSkin.accentColor,
                        totalLevelsInGame = levels.size,
                        onAchievementUnlocked = { pushAchievementToasts(it) },
                        onTimeAttackRetry = { if (playingTimeAttack) timeAttackRestartKey++ },
                        onPauseToMainMenu = {
                            playingTimeAttack = false
                            screen = PlatformerDestination.MainMenu
                        },
                        audioEnabled = audioEnabled,
                        onToggleAudio = ::toggleAudio,
                        appLanguage = appLanguage
                    )
                }
            }

            PlatformerDestination.LevelComplete -> {
                val taSize = timeAttackLevels.size
                ResultScreen(
                    title = if (playingTimeAttack) {
                        String.format(uiText.levelCompleteTa, currentLevel)
                    } else {
                        String.format(uiText.levelComplete, currentLevel)
                    },
                    subtitle = buildResultSubtitle(lastLevelResult, uiText),
                    primaryLabel = when {
                        playingTimeAttack && currentLevel < taSize -> uiText.next
                        playingTimeAttack -> uiText.toTimeAttackMode
                        currentLevel < levels.size -> uiText.nextLevel
                        else -> uiText.toMenu
                    },
                    secondaryLabel = uiText.replay,
                    onPrimary = {
                        when {
                            playingTimeAttack && currentLevel < taSize -> {
                                currentLevel += 1
                                screen = PlatformerDestination.Playing
                            }
                            playingTimeAttack -> {
                                screen = PlatformerDestination.TimeAttackLevelSelect
                            }
                            currentLevel < levels.size -> {
                                currentLevel += 1
                                screen = PlatformerDestination.Playing
                            }
                            else -> screen = PlatformerDestination.MainMenu
                        }
                    },
                    onSecondary = {
                        if (playingTimeAttack) timeAttackRestartKey++
                        screen = PlatformerDestination.Playing
                    },
                    tertiaryLabel = uiText.mainMenu,
                    onTertiary = {
                        if (playingTimeAttack) playingTimeAttack = false
                        screen = PlatformerDestination.MainMenu
                    }
                )
            }

            PlatformerDestination.GameComplete -> {
                ResultScreen(
                    title = uiText.gameCompleteTitle,
                    subtitle = buildResultSubtitle(lastLevelResult, uiText),
                    primaryLabel = uiText.playAgain,
                    secondaryLabel = uiText.mainMenu,
                    onPrimary = {
                        playingTimeAttack = false
                        currentLevel = 1
                        screen = PlatformerDestination.Playing
                    },
                    onSecondary = {
                        playingTimeAttack = false
                        screen = PlatformerDestination.MainMenu
                    }
                )
            }
            }
        }

        val banner = achievementBanner
        if (banner != null) {
            val bannerBottomPad =
                if (screen == PlatformerDestination.Playing) 140.dp else 12.dp
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        start = 20.dp,
                        top = 12.dp,
                        end = 20.dp,
                        bottom = bannerBottomPad
                    ),
                contentAlignment = Alignment.BottomCenter
            ) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.inverseSurface.copy(alpha = 0.92f)
                    ),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(
                        text = banner,
                        modifier = Modifier.padding(horizontal = 18.dp, vertical = 14.dp),
                        color = MaterialTheme.colorScheme.inverseOnSurface,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
    }
    }
}

@Preview(showBackground = true)
@Composable
fun GamePreview() {
    GameTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            PlatformerApp()
        }
    }
}
