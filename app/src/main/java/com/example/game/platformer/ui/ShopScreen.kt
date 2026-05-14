package com.example.game.platformer.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.game.platformer.PlayerSkinOption
import com.example.game.platformer.audio.SoundEvent
import com.example.game.platformer.localization.LocalAppLanguage
import com.example.game.platformer.localization.gameStrings
import com.example.game.platformer.localization.skinLocalizedName
import com.example.game.platformer.playerSkinCatalog

@Composable
fun ShopScreen(
    totalCoins: Int,
    ownedSkinIds: Set<Int>,
    selectedSkinId: Int,
    onBuySkin: (PlayerSkinOption) -> Boolean,
    onEquipSkin: (Int) -> Unit,
    onBack: () -> Unit
) {
    val lang = LocalAppLanguage.current
    val s = remember(lang) { gameStrings(lang) }
    val catalog = playerSkinCatalog()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Text(
            s.shopTitle,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            "${s.shopCoinsLine}$totalCoins",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
        )
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(catalog, key = { it.id }) { skin ->
                val owned = skin.id in ownedSkinIds
                val equipped = skin.id == selectedSkinId
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f))
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(skin.mainColor)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.BottomCenter)
                                    .height(12.dp)
                                    .background(skin.accentColor.copy(alpha = 0.88f))
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(skinLocalizedName(skin, lang), fontWeight = FontWeight.SemiBold)
                            Text(
                                if (owned) s.owned else "${skin.priceCoins}${s.coinsPriceSuffix}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        if (!owned) {
                            PlatformerPrimaryButton(
                                onClick = { onBuySkin(skin) },
                                enabled = totalCoins >= skin.priceCoins && skin.priceCoins > 0,
                                compact = true
                            ) {
                                Text(s.buy, fontWeight = FontWeight.Bold)
                            }
                        } else if (equipped) {
                            Text(
                                s.equipped,
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                        } else {
                            PlatformerSecondaryButton(
                                onClick = { onEquipSkin(skin.id) },
                                compact = true,
                                clickSound = SoundEvent.UI_EQUIP
                            ) {
                                Text(s.equip, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }
            }
        }
        PlatformerBackOutlinedButton(
            onClick = onBack,
            text = s.back,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )
    }
}
