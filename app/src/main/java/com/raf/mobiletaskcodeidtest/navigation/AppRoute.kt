package com.raf.mobiletaskcodeidtest.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class AppRoute {
    @Serializable
    object Auth : AppRoute() // Auth Parent

    @Serializable
    object Login : AppRoute()

    @Serializable
    object Register : AppRoute()

    @Serializable
    object MainMenu : AppRoute()
}