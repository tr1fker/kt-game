package com.example.game.platformer.audio


enum class SoundEvent(val fileName: String) {
    UI_CLICK("ui_click.ogg"),
    UI_BACK("ui_back.ogg"),
    UI_PRIMARY("ui_primary.ogg"),
    UI_EQUIP("ui_equip.ogg"),

    GAME_JUMP("game_jump.ogg"),
    GAME_DEATH("game_death.ogg"),
    GAME_CHECKPOINT("game_checkpoint.ogg"),
    GAME_COIN("game_coin.ogg"),
    GAME_GOAL("game_goal.ogg"),
    GAME_PAUSE_OPEN("game_pause_open.ogg"),
    GAME_PAUSE_CLOSE("game_pause_close.ogg"),
    GAME_LASER_WARN("game_laser_warn.ogg"),
    GAME_LASER_FIRE("game_laser_fire.ogg"),
    GAME_TIME_UP("game_time_up.ogg"),

    
    GAME_TELEPORT("game_teleport.ogg"),
    
    GAME_SPRING("game_spring.ogg"),
    
    GAME_KEY_PICKUP("game_key_pickup.ogg"),
    
    GAME_LAND("game_land.ogg"),
    
    GAME_BLOCK_HIT("game_block_hit.ogg"),
    
    GAME_BLOCK_BREAK("game_block_break.ogg"),
    
    GAME_PRESSURE_PLATE("game_pressure_plate.ogg"),

    ACHIEVEMENT("achievement.ogg")
}
