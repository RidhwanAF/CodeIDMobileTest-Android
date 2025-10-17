package com.raf.mobiletaskcodeidtest.settings.data.model.mapper

import com.raf.mobiletaskcodeidtest.core.domain.model.AppSettings
import com.raf.mobiletaskcodeidtest.core.domain.model.DarkTheme
import com.raf.mobiletaskcodeidtest.settings.data.model.DarkThemeData
import com.raf.mobiletaskcodeidtest.settings.data.model.SettingsData

object SettingsToData {
    fun AppSettings.toData() = SettingsData(
        darkTheme = darkTheme.toData(),
        dynamicColor = dynamicColor
    )

    private fun DarkTheme.toData() = DarkThemeData.valueOf(name)
}
