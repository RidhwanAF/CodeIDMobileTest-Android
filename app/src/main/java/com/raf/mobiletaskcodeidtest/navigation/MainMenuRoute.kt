package com.raf.mobiletaskcodeidtest.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class MainMenuRoute {
    @Serializable
    object Home : MainMenuRoute()

    @Serializable
    object Profile : MainMenuRoute()
}