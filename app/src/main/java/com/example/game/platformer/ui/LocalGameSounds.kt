package com.example.game.platformer.ui

import androidx.compose.runtime.staticCompositionLocalOf
import com.example.game.platformer.audio.GameSoundPlayer

val LocalGameSounds = staticCompositionLocalOf<GameSoundPlayer?> { null }
