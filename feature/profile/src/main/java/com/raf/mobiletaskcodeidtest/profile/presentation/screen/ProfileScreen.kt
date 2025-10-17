package com.raf.mobiletaskcodeidtest.profile.presentation.screen

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.raf.mobiletaskcodeidtest.core.presentation.component.FullScreenImageDialog
import com.raf.mobiletaskcodeidtest.core.presentation.component.LogoutDialog
import com.raf.mobiletaskcodeidtest.core.presentation.component.LogoutIconButton
import com.raf.mobiletaskcodeidtest.core.presentation.component.SettingsDialog
import com.raf.mobiletaskcodeidtest.core.presentation.component.SettingsIconButton
import com.raf.mobiletaskcodeidtest.profile.presentation.components.ProfilePictureView
import com.raf.mobiletaskcodeidtest.profile.presentation.viewmodel.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val scrollState = rememberScrollState()

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var dialogState by rememberSaveable {
        mutableStateOf(ProfileDialogState.NONE)
    }

    val pickMedia =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri == null) return@rememberLauncherForActivityResult
            viewModel.onEditProfilePicture(uri)
        }

    // Error Message
    LaunchedEffect(uiState.errorMessage) {
        if (uiState.errorMessage != null) {
            Toast.makeText(context, uiState.errorMessage, Toast.LENGTH_SHORT).show()
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(text = "Profile")
                    },
                    actions = {
                        SettingsIconButton(
                            visible = dialogState != ProfileDialogState.SETTINGS,
                            onClick = {
                                dialogState = ProfileDialogState.SETTINGS
                            }
                        )
                        LogoutIconButton(
                            visible = dialogState != ProfileDialogState.LOGOUT,
                            onClick = {
                                dialogState = ProfileDialogState.LOGOUT
                            }
                        )
                    },
                    scrollBehavior = scrollBehavior
                )
            },
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection)
        ) { innerPadding ->
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(innerPadding)
            ) {
                ProfilePictureView(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    visible = dialogState != ProfileDialogState.PROFILE_PICTURE,
                    label = uiState.profile?.name
                        ?: stringResource(com.couchbase.lite.R.string.app_name),
                    bitmap = viewModel.loadProfileImageBitmap(),
                    onEditClick = {
                        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    },
                    onClick = {
                        dialogState = ProfileDialogState.PROFILE_PICTURE
                    }
                )
            }
        }

        /**
         * Dialogs
         */
        SettingsDialog(
            isShown = dialogState == ProfileDialogState.SETTINGS,
            onDismissRequest = {
                dialogState = ProfileDialogState.NONE
            },
            currentAppSettings = uiState.appSettings,
            onAppSettingsChange = {
                viewModel.setAppSettings(it)
            }
        )

        LogoutDialog(
            isShown = dialogState == ProfileDialogState.LOGOUT,
            onDismissRequest = {
                dialogState = ProfileDialogState.NONE
            },
            onLogout = {
                viewModel.logout()
                dialogState = ProfileDialogState.NONE
            }
        )

        FullScreenImageDialog(
            visible = dialogState == ProfileDialogState.PROFILE_PICTURE,
            onDismissRequest = {
                dialogState = ProfileDialogState.NONE
            },
            bitmap = viewModel.loadProfileImageBitmap() ?: return@Box,
            label = uiState.profile?.name ?: stringResource(com.couchbase.lite.R.string.app_name)
        )
    }
}