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

    PlayerSkinOption(4, "Мята", "Mint", 100, Color(0xFF00695C), Color(0xFF80CBC4))

)



fun skinById(id: Int): PlayerSkinOption =

    playerSkinCatalog().firstOrNull { it.id == id } ?: playerSkinCatalog().first()

