package com.raf.mobiletaskcodeidtest.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.MainMenuNavGraph(
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = MainMenuRoute.Home
    ) {
        composable<MainMenuRoute.Home> {

        }
        composable<MainMenuRoute.Profile> {

        }
    }
}