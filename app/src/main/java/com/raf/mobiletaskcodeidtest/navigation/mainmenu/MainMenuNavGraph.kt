package com.raf.mobiletaskcodeidtest.navigation.mainmenu

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.raf.mobiletaskcodeidtest.home.presentation.screen.HomeScreen
import com.raf.mobiletaskcodeidtest.profile.presentation.screen.CreateProfileScreen
import com.raf.mobiletaskcodeidtest.profile.presentation.screen.IntroductionScreen
import com.raf.mobiletaskcodeidtest.profile.presentation.screen.ProfileScreen
import com.raf.mobiletaskcodeidtest.ui.MainMenuBottomBar

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.MainMenuNavGraph(
    isUserHasProfileAlready: Boolean,
) {
    val context = LocalContext.current
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentMenu =
        listMainMenuBottomBar.find { route ->
            route.route.isOnThisRoute(context, currentBackStackEntry)
        }

    Scaffold(
        bottomBar = {
            AnimatedVisibility(
                visible = currentMenu?.route == MainMenuRoute.Home || currentMenu?.route == MainMenuRoute.Profile,
                enter = slideInVertically { it } + scaleIn(),
                exit = scaleOut() + slideOutVertically { it }
            ) {
                MainMenuBottomBar(
                    currentRoute = currentMenu?.route ?: MainMenuRoute.Home,
                    onNavigate = {
                        navController.navigate(it.route) {
                            launchSingleTop = true
                            popUpTo(it.route) {
                                inclusive = true
                            }
                        }
                    }
                )
            }
        },
        modifier = Modifier.fillMaxSize()
    ) { parentPadding ->
        NavHost(
            navController = navController,
            startDestination = if (isUserHasProfileAlready) MainMenuRoute.Home else MainMenuRoute.Introduction
        ) {
            composable<MainMenuRoute.Home> {
                HomeScreen(
                    parentPaddingValues = parentPadding
                )
            }
            composable<MainMenuRoute.Introduction> {
                IntroductionScreen(
                    animatedContentScope = this@composable,
                    onNavigateToCreateProfile = {
                        navController.navigate(MainMenuRoute.CreateProfile) {
                            launchSingleTop = true
                        }
                    }
                )
            }
            composable<MainMenuRoute.CreateProfile> {
                CreateProfileScreen(
                    animatedContentScope = this@composable,
                    onNavigateToHome = {
                        navController.navigate(MainMenuRoute.Home) {
                            launchSingleTop = true
                            popUpTo(0) {
                                inclusive = true
                            }
                        }
                    }
                )
            }
            composable<MainMenuRoute.Profile> {
                ProfileScreen(
                    parentPaddingValues = parentPadding
                )
            }
        }
    }
}