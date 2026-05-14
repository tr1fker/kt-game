package com.example.game.platformer.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun ResultScreen(
    title: String,
    subtitle: String,
    primaryLabel: String,
    secondaryLabel: String,
    onPrimary: () -> Unit,
    onSecondary: () -> Unit,
    tertiaryLabel: String? = null,
    onTertiary: (() -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text(subtitle, modifier = Modifier.padding(top = 8.dp, bottom = 20.dp))
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                PlatformerPrimaryButton(onClick = onPrimary) {
                    Text(primaryLabel, fontWeight = FontWeight.SemiBold)
                }
                PlatformerSecondaryButton(onClick = onSecondary) {
                    Text(secondaryLabel, fontWeight = FontWeight.SemiBold)
                }
            }
            if (tertiaryLabel != null && onTertiary != null) {
                Spacer(modifier = Modifier.height(12.dp))
                PlatformerTertiaryButton(onClick = onTertiary) {
                    Text(tertiaryLabel, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}
