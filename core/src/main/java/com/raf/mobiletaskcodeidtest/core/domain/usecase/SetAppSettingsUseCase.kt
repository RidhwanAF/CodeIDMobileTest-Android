package com.raf.mobiletaskcodeidtest.core.domain.usecase

import com.raf.mobiletaskcodeidtest.core.domain.contract.AppSettingsProvider
import com.raf.mobiletaskcodeidtest.core.domain.model.AppSettings

class SetAppSettingsUseCase(private val appSettingsProvider: AppSettingsProvider) {
    suspend operator fun invoke(appSettings: AppSettings) {
        appSettingsProvider.setAppSettings(appSettings)
    }
}