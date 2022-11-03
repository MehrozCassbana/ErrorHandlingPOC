package com.bykea.task.utils.theme

import androidx.appcompat.app.AppCompatDelegate

object ThemeManager {

    const val LIGHT_MODE = "Light"
    const val DARK_MODE = "Dark"
    const val AUTO_BATTERY_MODE = "Auto-battery"
    const val FOLLOW_SYSTEM_MODE = "System"

    fun applyTheme(themePreference: String?) {
        when (themePreference) {
            LIGHT_MODE -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            DARK_MODE -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            AUTO_BATTERY_MODE -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY)
            FOLLOW_SYSTEM_MODE -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            else -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
    }
}