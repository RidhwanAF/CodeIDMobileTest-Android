package com.raf.mobiletaskcodeidtest.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun AppNavGraph(
    navController: NavHostController,
    startDestination: AppRoute,
) {
    SharedTransitionLayout(
        modifier = Modifier.fillMaxSize()
    ) {
        NavHost(
            navController = navController,
            startDestination = startDestination
        ) {
            navigation<AppRoute.Auth>(
                startDestination = AppRoute.Login,
            ) {
                composable<AppRoute.Login> {

                }
                composable<AppRoute.Register> {

                }
            }

            composable<AppRoute.MainMenu> {
                MainMenuNavGraph()
            }
        }
    }
}