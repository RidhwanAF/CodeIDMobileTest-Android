package com.raf.mobiletaskcodeidtest.navigation.mainmenu

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.ui.graphics.vector.ImageVector

data class MainMenu(
    val title: String,
    val icon: ImageVector,
    val activeIcon: ImageVector,
    val route: MainMenuRoute,
)

val listMainMenuBottomBar = listOf(
    MainMenu(
        title = "Home",
        icon = Icons.Outlined.Home,
        activeIcon = Icons.Default.Home,
        route = MainMenuRoute.Home
    ),
    MainMenu(
        title = "Profile",
        icon = Icons.Outlined.Person,
        activeIcon = Icons.Default.Person,
        route = MainMenuRoute.Profile
    ),
)