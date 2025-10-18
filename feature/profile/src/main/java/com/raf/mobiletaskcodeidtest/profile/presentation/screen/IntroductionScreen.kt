package com.raf.mobiletaskcodeidtest.profile.presentation.screen

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.raf.mobiletaskcodeidtest.core.R

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.IntroductionScreen(
    animatedContentScope: AnimatedContentScope,
    onNavigateToCreateProfile: () -> Unit,
) {
    val scrollState = rememberScrollState()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(innerPadding)
        ) {
            Text(
                text = "Welcome to ${stringResource(R.string.app_name)}",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Before you start, let's create your profile!",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
            Button(
                onClick = onNavigateToCreateProfile,
                modifier = Modifier
                    .sharedElement(
                        sharedContentState = rememberSharedContentState("introduction-button"),
                        animatedVisibilityScope = animatedContentScope
                    )
                    .padding(16.dp)
            ) {
                Text(text = "Tap to Create Profile")
            }
        }
    }
}