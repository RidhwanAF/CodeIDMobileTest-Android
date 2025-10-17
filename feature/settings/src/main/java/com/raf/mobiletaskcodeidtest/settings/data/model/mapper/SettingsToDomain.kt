package com.raf.mobiletaskcodeidtest.settings.data.model.mapper

import com.raf.mobiletaskcodeidtest.core.domain.model.AppSettings
import com.raf.mobiletaskcodeidtest.core.domain.model.DarkTheme
import com.raf.mobiletaskcodeidtest.settings.data.model.DarkThemeData
import com.raf.mobiletaskcodeidtest.settings.data.model.SettingsData

object SettingsToDomain {
    fun SettingsData.toDomain() = AppSettings(
        darkTheme = darkTheme.toDomain(),
        dynamicColor = dynamicColor
    )

    private fun DarkThemeData.toDomain() = DarkTheme.valueOf(name)
}