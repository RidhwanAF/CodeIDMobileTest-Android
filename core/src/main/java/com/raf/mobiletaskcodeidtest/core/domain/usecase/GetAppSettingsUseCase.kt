package com.raf.mobiletaskcodeidtest.core.domain.usecase

import com.raf.mobiletaskcodeidtest.core.domain.contract.AppSettingsProvider

class GetAppSettingsUseCase(private val appSettingsProvider: AppSettingsProvider) {
    operator fun invoke() = appSettingsProvider.getAppSettings()
}