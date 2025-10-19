package com.raf.mobiletaskcodeidtest.navigation.mainmenu

import android.content.Context
import androidx.navigation.NavBackStackEntry
import kotlinx.serialization.Serializable

@Serializable
sealed class MainMenuRoute {
    @Serializable
    data object Introduction : MainMenuRoute()

    @Serializable
    data object Home : MainMenuRoute()

    @Serializable
    data class Detail(
        val id: String? = null,
        val name: String? = null,
    ) : MainMenuRoute()

    @Serializable
    data object CreateProfile : MainMenuRoute()

    @Serializable
    data object Profile : MainMenuRoute()
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
