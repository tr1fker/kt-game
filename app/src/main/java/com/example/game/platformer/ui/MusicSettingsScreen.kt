package com.example.game.platformer.ui

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.game.platformer.audio.BGM_IMPORTED_PREFIX
import com.example.game.platformer.audio.GameMusicPlayer
import com.example.game.platformer.audio.allBgmTrackIds
import com.example.game.platformer.audio.coerceLevelPlaylist
import com.example.game.platformer.audio.coerceMenuPlaylist
import com.example.game.platformer.audio.deleteImportedBgmTrack
import com.example.game.platformer.audio.displayTrackTitle
import com.example.game.platformer.audio.importBgmFromPickerUri
import com.example.game.platformer.audio.loadLevelBgmPlaylist
import com.example.game.platformer.audio.loadMenuBgmPlaylist
import com.example.game.platformer.audio.MusicTrackPreviewPlayer
import com.example.game.platformer.audio.saveLevelBgmPlaylist
import com.example.game.platformer.audio.saveMenuBgmPlaylist
import com.example.game.platformer.localization.LocalAppLanguage
import com.example.game.platformer.localization.gameStrings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private val deleteColWidth = 40.dp

@Composable
fun MusicSettingsScreen(
    onBack: () -> Unit,
    onSaved: () -> Unit,
    musicPlayer: GameMusicPlayer,
) {
    val context = LocalContext.current
    val app = context.applicationContext
    val lang = LocalAppLanguage.current
    val s = remember(lang) { gameStrings(lang) }
    var trackRefresh by remember { mutableIntStateOf(0) }
    val tracks = remember(trackRefresh) { allBgmTrackIds(app) }
    var menuSel by remember { mutableStateOf(loadMenuBgmPlaylist(app).toSet()) }
    var levelSel by remember { mutableStateOf(loadLevelBgmPlaylist(app).toSet()) }
    var previewPath by remember { mutableStateOf<String?>(null) }
    val previewPlayer = remember(app, musicPlayer) {
        MusicTrackPreviewPlayer(
            app,
            onPauseMainBgm = { musicPlayer.pauseForPreview() },
            onResumeMainBgm = { musicPlayer.tryResumeAfterPreview() },
            onActivePreviewPathChanged = { previewPath = it },
        )
    }
    DisposableEffect(previewPlayer) {
        onDispose { previewPlayer.release() }
    }

    val scope = rememberCoroutineScope()
    val pickLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri == null) return@rememberLauncherForActivityResult
        scope.launch {
            val result = withContext(Dispatchers.IO) { importBgmFromPickerUri(app, uri) }
            withContext(Dispatchers.Main) {
                result.fold(
                    onSuccess = { id ->
                        trackRefresh++
                        menuSel = menuSel + id
                        levelSel = levelSel + id
                        Toast.makeText(context, s.musicImportSuccess, Toast.LENGTH_SHORT).show()
                    },
                    onFailure = {
                        Toast.makeText(context, s.musicImportFailed, Toast.LENGTH_SHORT).show()
                    },
                )
            }
        }
    }

    fun onAddFromDevice() {
        pickLauncher.launch(
            arrayOf(
                "audio/*",
                "application/ogg",
                "audio/mpeg",
                "audio/x-wav",
            ),
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
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
                text = s.musicSettingsTitle,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
        }
        Text(
            text = s.musicSettingsHint,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 8.dp, bottom = 12.dp)
        )

        PlatformerSecondaryButton(
            onClick = { onAddFromDevice() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        ) {
            Text(s.musicAddFromDevice, fontWeight = FontWeight.SemiBold)
        }

        if (tracks.isEmpty()) {
            Text(
                text = s.musicNoTracks,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 8.dp)
            )
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = s.musicColumnTrack,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(deleteColWidth))
                Spacer(modifier = Modifier.width(48.dp))
                Text(
                    text = s.musicColumnMenu,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(end = 4.dp)
                )
                Text(
                    text = s.musicColumnLevel,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold
                )
            }

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(tracks, key = { it }) { path ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = displayTrackTitle(path),
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.weight(1f),
                            maxLines = 2
                        )
                        Box(
                            modifier = Modifier.width(deleteColWidth),
                            contentAlignment = Alignment.Center
                        ) {
                            if (path.startsWith(BGM_IMPORTED_PREFIX)) {
                                IconButton(
                                    onClick = {
                                        if (previewPath == path) previewPlayer.stop()
                                        menuSel = menuSel - path
                                        levelSel = levelSel - path
                                        deleteImportedBgmTrack(app, path)
                                        trackRefresh++
                                    },
                                    modifier = Modifier
                                        .size(40.dp)
                                        .semantics { contentDescription = s.musicDeleteImported }
                                ) {
                                    Text(
                                        text = "✕",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.error,
                                        textAlign = TextAlign.Center,
                                    )
                                }
                            }
                        }
                        IconButton(
                            onClick = { previewPlayer.toggle(path) },
                            modifier = Modifier.size(48.dp)
                        ) {
                            val playing = previewPath == path
                            Icon(
                                imageVector = if (playing) Icons.Filled.Close else Icons.Filled.PlayArrow,
                                contentDescription = if (playing) s.musicPreviewStop else s.musicPreviewPlay,
                            )
                        }
                        Checkbox(
                            checked = path in menuSel,
                            onCheckedChange = { on ->
                                menuSel = if (on) menuSel + path else menuSel - path
                            }
                        )
                        Checkbox(
                            checked = path in levelSel,
                            onCheckedChange = { on ->
                                levelSel = if (on) levelSel + path else levelSel - path
                            }
                        )
                    }
                }
            }

            PlatformerPrimaryButton(
                onClick = {
                    val menuFinal = coerceMenuPlaylist(app, menuSel)
                    val levelFinal = coerceLevelPlaylist(app, levelSel)
                    saveMenuBgmPlaylist(app, menuFinal)
                    saveLevelBgmPlaylist(app, levelFinal)
                    menuSel = menuFinal.toSet()
                    levelSel = levelFinal.toSet()
                    onSaved()
                    onBack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            ) {
                Text(s.musicSave, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}
