package com.raf.mobiletaskcodeidtest.profile.presentation.screen

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.raf.mobiletaskcodeidtest.core.presentation.component.FullScreenImageDialog
import com.raf.mobiletaskcodeidtest.profile.presentation.components.ProfilePictureView
import com.raf.mobiletaskcodeidtest.profile.presentation.viewmodel.ProfileViewModel

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SharedTransitionScope.CreateProfileScreen(
    animatedContentScope: AnimatedContentScope,
    viewModel: ProfileViewModel = hiltViewModel(),
    onNavigateToHome: () -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val scrollState = rememberScrollState()

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val pickMedia =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri == null) return@rememberLauncherForActivityResult
            viewModel.onProfilePictureInputChange(uri)
        }

    var showFullScreenImageDialog by rememberSaveable {
        mutableStateOf(false)
    }

    val profileImageBitmap = remember(viewModel.profilePictureInput) {
        viewModel.loadProfileImageBitmap(viewModel.profilePictureInput)
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = "Create Profile",
                            style = MaterialTheme.typography.titleLarge
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
                verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Top),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp)
            ) {
                Spacer(modifier = Modifier.height(32.dp))
                ProfilePictureView(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    visible = !showFullScreenImageDialog,
                    label = viewModel.nameInput.ifBlank { stringResource(com.couchbase.lite.R.string.app_name) },
                    bitmap = profileImageBitmap,
                    onEditClick = {
                        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    },
                    onClick = { showFullScreenImageDialog = true }
                )
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(max = 220.dp)
                )
                Button(
                    onClick = {
                        viewModel.registerProfile(
                            onFinish = {
                                onNavigateToHome.invoke()
                                viewModel.resetInputs()
                            }
                        )
                    },
                    enabled = viewModel.nameInput.isNotBlank(),
                    modifier = Modifier
                        .sharedElement(
                            sharedContentState = rememberSharedContentState("introduction-button"),
                            animatedVisibilityScope = animatedContentScope
                        )
                        .fillMaxWidth()
                        .widthIn(max = 220.dp)
                ) {
                    Text(text = "Create Now")
                }
            }
        }
        FullScreenImageDialog(
            visible = showFullScreenImageDialog,
            onDismissRequest = { showFullScreenImageDialog = false },
            bitmap = viewModel.loadProfileImageBitmap(viewModel.profilePictureInput) ?: return@Box,
            label = uiState.profile?.name ?: stringResource(com.couchbase.lite.R.string.app_name)
        )
    }
}