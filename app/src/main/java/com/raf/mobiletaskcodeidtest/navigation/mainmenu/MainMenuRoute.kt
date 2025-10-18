package com.raf.mobiletaskcodeidtest.navigation.mainmenu

import android.content.Context
import androidx.navigation.NavBackStackEntry
import kotlinx.serialization.Serializable

@Serializable
sealed class MainMenuRoute {
    @Serializable
    object Introduction : MainMenuRoute()

    @Serializable
    object Home : MainMenuRoute()

    @Serializable
    object CreateProfile : MainMenuRoute()

    @Serializable
    object Profile : MainMenuRoute()
}

fun MainMenuRoute.isOnThisRoute(
    context: Context,
    currentBackStackEntry: NavBackStackEntry?,
): Boolean {
    val currentRoute = currentBackStackEntry?.destination?.route
    val thisRoute =
        "${context.applicationContext.packageName}.navigation.mainmenu.MainMenuRoute.${javaClass.simpleName}"
    return currentRoute == thisRoute
}
