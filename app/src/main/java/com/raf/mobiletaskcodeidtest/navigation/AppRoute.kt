package com.raf.mobiletaskcodeidtest.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class AppRoute {
    @Serializable
    data object Auth : AppRoute() // Auth Parent

    @Serializable
    data object Login : AppRoute()

    @Serializable
    data object Register : AppRoute()

    @Serializable
    data object MainMenu : AppRoute()
}