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
import com.raf.mobiletaskcodeidtest.auth.presentation.screen.LoginScreen
import com.raf.mobiletaskcodeidtest.auth.presentation.screen.RegisterScreen

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun AppNavGraph(
    navController: NavHostController,
    startDestination: AppRoute,
    isUserHasProfileAlready: Boolean,
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
                    LoginScreen(
                        animatedContentScope = this@composable,
                        onNavigateToRegister = {
                            navController.navigate(AppRoute.Register) {
                                launchSingleTop = true
                            }
                        }
                    )
                }
                composable<AppRoute.Register> {
                    RegisterScreen(
                        animatedContentScope = this@composable,
                        onNavigateToLogin = {
                            navController.navigateUp()
                        }
                    )
                }
            }

            composable<AppRoute.MainMenu> {
                MainMenuNavGraph(
                    isUserHasProfileAlready = isUserHasProfileAlready
                )
            }
        }
    }
}