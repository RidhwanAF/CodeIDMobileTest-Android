package com.raf.mobiletaskcodeidtest.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(text = "Home")
            }
        }
        composable<MainMenuRoute.Profile> {
            ProfileScreen()
        }
    }
}