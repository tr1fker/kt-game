package com.example.game.platformer.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.game.platformer.audio.SoundEvent


val PlatformerMenuButtonShape = RoundedCornerShape(14.dp)

private val BtnPrimary = Color(0xFF1FA89A)
private val BtnSecondary = Color(0xFF3D4B5C)
private val BtnSecondaryDeep = Color(0xFF2F3A48)
private val BtnMuted = Color(0xFF4A5869)
private val BtnOnLight = Color(0xFFF5FAFF)
private val BtnOutline = Color(0xFF5C6F82)
private val BtnDisabledFill = Color(0xFF2A323C)
private val BtnDisabledOn = Color(0xFF8A95A3)

@Composable
fun PlatformerPrimaryButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    compact: Boolean = false,
    clickSound: SoundEvent = SoundEvent.UI_PRIMARY,
    content: @Composable RowScope.() -> Unit
) {
    val sounds = LocalGameSounds.current
    val sizeMod = if (compact) modifier else modifier.heightIn(min = 48.dp)
    val vPad = if (compact) 6.dp else 14.dp
    val hPad = if (compact) 14.dp else 22.dp
    Button(
        onClick = {
            sounds?.play(clickSound)
            onClick()
        },
        modifier = sizeMod,
        enabled = enabled,
        shape = PlatformerMenuButtonShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = BtnPrimary,
            contentColor = Color.White,
            disabledContainerColor = BtnDisabledFill.copy(alpha = 0.72f),
            disabledContentColor = BtnDisabledOn
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 5.dp,
            pressedElevation = 2.dp,
            disabledElevation = 0.dp
        ),
        contentPadding = PaddingValues(horizontal = hPad, vertical = vPad),
        content = content
    )
}

@Composable
fun PlatformerSecondaryButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    compact: Boolean = false,
    clickSound: SoundEvent = SoundEvent.UI_CLICK,
    content: @Composable RowScope.() -> Unit
) {
    val sounds = LocalGameSounds.current
    val sizeMod = if (compact) modifier else modifier.heightIn(min = 46.dp)
    val vPad = if (compact) 6.dp else 12.dp
    val hPad = if (compact) 14.dp else 20.dp
    Button(
        onClick = {
            sounds?.play(clickSound)
            onClick()
        },
        modifier = sizeMod,
        enabled = enabled,
        shape = PlatformerMenuButtonShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = BtnSecondary,
            contentColor = BtnOnLight,
            disabledContainerColor = BtnDisabledFill.copy(alpha = 0.55f),
            disabledContentColor = BtnDisabledOn
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 3.dp,
            pressedElevation = 1.dp,
            disabledElevation = 0.dp
        ),
        contentPadding = PaddingValues(horizontal = hPad, vertical = vPad),
        content = content
    )
}

@Composable
fun PlatformerTertiaryButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    compact: Boolean = false,
    clickSound: SoundEvent = SoundEvent.UI_CLICK,
    content: @Composable RowScope.() -> Unit
) {
    val sounds = LocalGameSounds.current
    val sizeMod = if (compact) modifier else modifier.heightIn(min = 46.dp)
    val vPad = if (compact) 6.dp else 12.dp
    val hPad = if (compact) 14.dp else 18.dp
    Button(
        onClick = {
            sounds?.play(clickSound)
            onClick()
        },
        modifier = sizeMod,
        enabled = enabled,
        shape = PlatformerMenuButtonShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = BtnSecondaryDeep,
            contentColor = BtnOnLight,
            disabledContainerColor = BtnDisabledFill.copy(alpha = 0.5f),
            disabledContentColor = BtnDisabledOn
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 2.dp,
            pressedElevation = 0.dp,
            disabledElevation = 0.dp
        ),
        contentPadding = PaddingValues(horizontal = hPad, vertical = vPad),
        content = content
    )
}

@Composable
fun PlatformerBackOutlinedButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    compact: Boolean = false
) {
    val sounds = LocalGameSounds.current
    val vPad = if (compact) 8.dp else 12.dp
    val hPad = if (compact) 14.dp else 18.dp
    val sizeMod = if (compact) modifier else modifier.heightIn(min = 46.dp)
    OutlinedButton(
        onClick = {
            sounds?.play(SoundEvent.UI_BACK)
            onClick()
        },
        modifier = sizeMod,
        shape = if (compact) RoundedCornerShape(12.dp) else PlatformerMenuButtonShape,
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        border = BorderStroke(1.5.dp, BtnOutline.copy(alpha = 0.85f)),
        contentPadding = PaddingValues(horizontal = hPad, vertical = vPad)
    ) {
        Text(text, fontWeight = FontWeight.SemiBold)
    }
}


@Composable
fun PlatformerHudButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = null,
    clickSound: SoundEvent = SoundEvent.UI_CLICK
) {
    val sounds = LocalGameSounds.current
    val src = interactionSource ?: remember { MutableInteractionSource() }
    Button(
        onClick = {
            sounds?.play(clickSound)
            onClick()
        },
        modifier = modifier.heightIn(min = 36.dp),
        enabled = enabled,
        interactionSource = src,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = BtnMuted,
            contentColor = BtnOnLight,
            disabledContainerColor = BtnDisabledFill.copy(alpha = 0.45f),
            disabledContentColor = BtnDisabledOn
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 3.dp,
            pressedElevation = 1.dp,
            disabledElevation = 0.dp
        ),
        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Text(text, fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.labelLarge)
    }
}


@Composable
fun PlatformerTimeAttackChipButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) {
    val sounds = LocalGameSounds.current
    val chipBg = Color(0xFF343B44)
    val chipFg = Color(0xFFE6E9EF)
    Button(
        onClick = {
            sounds?.play(SoundEvent.UI_CLICK)
            onClick()
        },
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = chipBg,
            contentColor = chipFg
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 3.dp, pressedElevation = 1.dp),
        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 8.dp),
        content = content
    )
}

object PlatformerLevelGridButtonDefaults {
    @Composable
    fun campaignColors(isUnlocked: Boolean) = ButtonDefaults.buttonColors(
        containerColor = if (isUnlocked) BtnSecondary else BtnDisabledFill.copy(alpha = 0.55f),
        contentColor = if (isUnlocked) BtnOnLight else BtnDisabledOn,
        disabledContainerColor = BtnDisabledFill.copy(alpha = 0.45f),
        disabledContentColor = BtnDisabledOn
    )

    @Composable
    fun timeAttackColors(isUnlocked: Boolean) = ButtonDefaults.buttonColors(
        containerColor = if (isUnlocked) Color(0xFF3A424C) else Color(0xFF2A3038).copy(alpha = 0.65f),
        contentColor = if (isUnlocked) Color(0xFFDFE3E8) else Color(0xFF9AA5B5),
        disabledContainerColor = Color(0xFF2A3038).copy(alpha = 0.5f),
        disabledContentColor = Color(0xFF7A8494)
    )
}
