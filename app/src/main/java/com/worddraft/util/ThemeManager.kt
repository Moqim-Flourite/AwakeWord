package com.worddraft.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * 主题模式枚举
 */
enum class ThemeMode {
    FOLLOW_SYSTEM,  // 跟随系统
    LIGHT,          // 白天模式
    DARK            // 黑夜模式
}

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "theme_settings")

/**
 * 主题设置管理器
 */
object ThemeManager {
    private val THEME_MODE_KEY = booleanPreferencesKey("theme_mode_light")
    private val THEME_MODE_DARK = booleanPreferencesKey("theme_mode_dark")
    
    /**
     * 获取主题模式
     */
    fun getThemeMode(context: Context): Flow<ThemeMode> {
        return context.dataStore.data.map { preferences ->
            val isLight = preferences[THEME_MODE_KEY] ?: false
            val isDark = preferences[THEME_MODE_DARK] ?: false
            when {
                isDark -> ThemeMode.DARK
                isLight -> ThemeMode.LIGHT
                else -> ThemeMode.FOLLOW_SYSTEM
            }
        }
    }
    
    /**
     * 设置主题模式
     */
    suspend fun setThemeMode(context: Context, mode: ThemeMode) {
        context.dataStore.edit { preferences ->
            when (mode) {
                ThemeMode.LIGHT -> {
                    preferences[THEME_MODE_KEY] = true
                    preferences[THEME_MODE_DARK] = false
                }
                ThemeMode.DARK -> {
                    preferences[THEME_MODE_KEY] = false
                    preferences[THEME_MODE_DARK] = true
                }
                ThemeMode.FOLLOW_SYSTEM -> {
                    preferences.remove(THEME_MODE_KEY)
                    preferences.remove(THEME_MODE_DARK)
                }
            }
        }
    }
}