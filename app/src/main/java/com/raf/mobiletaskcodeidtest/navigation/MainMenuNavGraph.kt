package com.raf.mobiletaskcodeidtest.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.raf.mobiletaskcodeidtest.home.presentation.screen.HomeScreen
import com.raf.mobiletaskcodeidtest.profile.presentation.screen.CreateProfileScreen
import com.raf.mobiletaskcodeidtest.profile.presentation.screen.IntroductionScreen
import com.raf.mobiletaskcodeidtest.profile.presentation.screen.ProfileScreen

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.MainMenuNavGraph(
    isUserHasProfileAlready: Boolean,
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = if (isUserHasProfileAlready) MainMenuRoute.Home else MainMenuRoute.Introduction
    ) {
        composable<MainMenuRoute.Home> {
            HomeScreen()
        }
        composable<MainMenuRoute.Introduction> {
            IntroductionScreen(
                onNavigateToCreateProfile = {
                    navController.navigate(MainMenuRoute.CreateProfile) {
                        launchSingleTop = true
                        popUpTo(MainMenuRoute.Introduction) {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable<MainMenuRoute.CreateProfile> {
            CreateProfileScreen(
                onNavigateToHome = {
                    navController.navigate(MainMenuRoute.Home) {
                        launchSingleTop = true
                        popUpTo(MainMenuRoute.CreateProfile) {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable<MainMenuRoute.Profile> {
            ProfileScreen()
        }
    }
}