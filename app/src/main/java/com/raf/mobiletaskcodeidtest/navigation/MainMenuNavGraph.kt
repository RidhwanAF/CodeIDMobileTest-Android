package com.raf.mobiletaskcodeidtest.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.raf.mobiletaskcodeidtest.home.presentation.screen.HomeScreen
import com.raf.mobiletaskcodeidtest.profile.presentation.screen.ProfileScreen

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.MainMenuNavGraph(
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = MainMenuRoute.Profile
    ) {
        composable<MainMenuRoute.Home> {
            HomeScreen()
        }
        composable<MainMenuRoute.Profile> {
            ProfileScreen()
        }
    }
}