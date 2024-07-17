package com.jeanpaulo.musiclibrary.settings.ui

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager

fun Context.getPreferenceTheme(resources: Resources): String? {
    val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
    return prefs.getString(
        resources.getString(R.string.preference_theme_key),
        resources.getString(R.string.preference_theme_value_default)
    )
}


fun applyTheme(resources: Resources, theme: String?) {
    when (theme) {
        resources.getString(R.string.preference_theme_value_light) -> AppCompatDelegate.setDefaultNightMode(
            AppCompatDelegate.MODE_NIGHT_NO
        )

        resources.getString(R.string.preference_theme_value_dark) -> AppCompatDelegate.setDefaultNightMode(
            AppCompatDelegate.MODE_NIGHT_YES
        )

        else -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
    }
}