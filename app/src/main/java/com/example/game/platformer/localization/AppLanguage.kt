package com.example.game.platformer.localization

enum class AppLanguage(val storageCode: String) {
    Russian("ru"),
    English("en");

    val flagEmoji: String
        get() = when (this) {
            Russian -> "🇷🇺"
            English -> "🇺🇸"
        }

    fun toggled(): AppLanguage = when (this) {
        Russian -> English
        English -> Russian
    }

    companion object {
        fun fromStorageCode(code: String?): AppLanguage =
            when (code) {
                English.storageCode -> English
                else -> Russian
            }
    }
}
