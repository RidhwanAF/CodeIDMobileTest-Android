package com.raf.mobiletaskcodeidtest

import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.raf.mobiletaskcodeidtest.core.domain.model.DarkTheme
import com.raf.mobiletaskcodeidtest.core.presentation.theme.MobileTaskCodeIdTestTheme
import com.raf.mobiletaskcodeidtest.navigation.AppNavGraph
import com.raf.mobiletaskcodeidtest.navigation.AppRoute
import com.raf.mobiletaskcodeidtest.viewmodel.AppState
import com.raf.mobiletaskcodeidtest.viewmodel.AppViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var appViewModel: AppViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Splash Screen
        val content: View = findViewById(android.R.id.content)
        content.viewTreeObserver.addOnPreDrawListener(
            object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    return if (appViewModel.appState.value is AppState.Loaded) {
                        content.viewTreeObserver.removeOnPreDrawListener(this)
                        true
                    } else false
                }
            }
        )

        setContent {
            appViewModel = hiltViewModel()
            val appState by appViewModel.appState.collectAsStateWithLifecycle()

            val navController = rememberNavController()
            val startDestination = remember(appState) {
                when (appState) {
                    is AppState.Loaded -> {
                        if ((appState as AppState.Loaded).isLoggedIn) AppRoute.MainMenu else AppRoute.Auth
                    }

                    else -> AppRoute.Auth
                }
            }

            (appState as? AppState.Loaded)?.let { appData ->
                val darkTheme = when (appData.appSettings.darkTheme) {
                    DarkTheme.FOLLOW_SYSTEM -> isSystemInDarkTheme()
                    DarkTheme.LIGHT -> false
                    DarkTheme.DARK -> true
                }

                MobileTaskCodeIdTestTheme(
                    darkTheme = darkTheme,
                    dynamicColor = appData.appSettings.dynamicColor
                ) {
                    AppNavGraph(
                        navController = navController,
                        startDestination = startDestination
                    )
                }
            }
        }
    }
}
