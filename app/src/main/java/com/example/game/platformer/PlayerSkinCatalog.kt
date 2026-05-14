package com.example.game.platformer

import androidx.compose.ui.graphics.Color

data class PlayerSkinOption(
    val id: Int,
    val nameRu: String,
    val nameEn: String,
    val priceCoins: Int,
    val mainColor: Color,
    val accentColor: Color
)

fun playerSkinCatalog(): List<PlayerSkinOption> = listOf(
    PlayerSkinOption(0, "Классика", "Classic", 0, Color(0xFF42A5F5), Color(0xFF90CAF9)),
    PlayerSkinOption(1, "Арбуз", "Watermelon", 45, Color(0xFFC62828), Color(0xFF66BB6A)),
    PlayerSkinOption(2, "Лимон", "Lemon", 60, Color(0xFFFFA000), Color(0xFFFFF176)),
    PlayerSkinOption(3, "Лаванда", "Lavender", 80, Color(0xFF5E35B1), Color(0xFFB39DDB)),
    PlayerSkinOption(4, "Мята", "Mint", 100, Color(0xFF00695C), Color(0xFF80CBC4)),
    PlayerSkinOption(5, "НЛО", "UFO", 115, Color(0xFF78909C), Color(0xFF76FF03)),
    PlayerSkinOption(6, "Сахарная вата", "Cotton Candy", 130, Color(0xFFEC407A), Color(0xFFE1F5FE)),
    PlayerSkinOption(7, "Кибер-поп", "Cyber Pop", 150, Color(0xFFD500F9), Color(0xFF00E5FF)),
    PlayerSkinOption(8, "Золотая рыбка", "Goldfish", 170, Color(0xFFFF6D00), Color(0xFFFFFDE7)),
    PlayerSkinOption(9, "Пингвин", "Tux", 190, Color(0xFF263238), Color(0xFFECEFF1)),
    PlayerSkinOption(10, "Слайм", "Slime", 210, Color(0xFF64DD17), Color(0xFFAA00FF)),
    PlayerSkinOption(11, "Лава", "Molten", 240, Color(0xFFBF360C), Color(0xFFFFEA00)),
    PlayerSkinOption(12, "Звёздная ночь", "Stardust", 280, Color(0xFF1A237E), Color(0xFFFFD740)),
    PlayerSkinOption(13, "Радуга", "Rainbow Pop", 310, Color(0xFFE040FB), Color(0xFF69F0AE)),
    PlayerSkinOption(14, "Ретро-8bit", "Retro 8-bit", 340, Color(0xFF33691E), Color(0xFFEEFF41)),
    PlayerSkinOption(15, "Космический жук", "Space Beetle", 380, Color(0xFF004D40), Color(0xFFFF6E40))
)

fun skinById(id: Int): PlayerSkinOption =
    playerSkinCatalog().firstOrNull { it.id == id } ?: playerSkinCatalog().first()
