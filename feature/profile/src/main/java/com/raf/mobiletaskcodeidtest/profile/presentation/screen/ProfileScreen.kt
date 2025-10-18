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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.raf.mobiletaskcodeidtest.core.presentation.component.FullScreenImageDialog
import com.raf.mobiletaskcodeidtest.core.presentation.component.LogoutButton
import com.raf.mobiletaskcodeidtest.core.presentation.component.LogoutDialog
import com.raf.mobiletaskcodeidtest.core.presentation.component.SettingsDialog
import com.raf.mobiletaskcodeidtest.core.presentation.component.SettingsIconButton
import com.raf.mobiletaskcodeidtest.profile.presentation.components.ProfileItemView
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
                    .padding(horizontal = 16.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                ProfilePictureView(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    visible = dialogState != ProfileDialogState.PROFILE_PICTURE,
                    label = uiState.profile?.name
                        ?: stringResource(com.couchbase.lite.R.string.app_name),
                    bitmap = viewModel.loadProfileImageBitmap(uiState.profile?.picturePath?.toUri()),
                    onEditClick = {
                        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    },
                    onClick = {
                        dialogState = ProfileDialogState.PROFILE_PICTURE
                    }
                )
                ProfileItemView(
                    label = "Name",
                    value = uiState.profile?.name ?: "",
                    onEditClick = {
                        viewModel.onNameInputChange(uiState.profile?.name ?: "")
                        dialogState = ProfileDialogState.EDIT_NAME
                    }
                )
                ProfileItemView(
                    label = "Email",
                    value = uiState.profile?.email ?: ""
                )
                LogoutButton(
                    visible = dialogState != ProfileDialogState.LOGOUT,
                    onLogout = {
                        dialogState = ProfileDialogState.LOGOUT
                    },
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }
        }

        /**
         * Dialogs
         */
        if (dialogState == ProfileDialogState.EDIT_NAME) {
            ModalBottomSheet(
                onDismissRequest = {
                    dialogState = ProfileDialogState.NONE
                    viewModel.onNameInputChange("")
                }
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .navigationBarsPadding()
                ) {
                    OutlinedTextField(
                        value = viewModel.nameInput,
                        onValueChange = {
                            viewModel.onNameInputChange(it)
                        },
                        label = {
                            Text(text = "Name")
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Done
                        ),
                        singleLine = true,
                        keyboardActions = KeyboardActions {
                            this.defaultKeyboardAction(ImeAction.Done)
                            if (viewModel.nameInput.isBlank()) return@KeyboardActions
                            viewModel.onEditProfileNameSave(
                                onFinish = {
                                    dialogState = ProfileDialogState.NONE
                                    viewModel.onNameInputChange("")
                                }
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Button(
                        enabled = viewModel.nameInput.isNotBlank() && viewModel.nameInput != uiState.profile?.name,
                        onClick = {
                            if (viewModel.nameInput.isBlank()) return@Button
                            viewModel.onEditProfileNameSave(
                                onFinish = {
                                    dialogState = ProfileDialogState.NONE
                                    viewModel.onNameInputChange("")
                                }
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Save")
                    }
                }
            }
        }

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
            bitmap = viewModel.loadProfileImageBitmap(uiState.profile?.picturePath?.toUri())
                ?: return@Box,
            label = uiState.profile?.name ?: stringResource(com.couchbase.lite.R.string.app_name)
        )
    }
}