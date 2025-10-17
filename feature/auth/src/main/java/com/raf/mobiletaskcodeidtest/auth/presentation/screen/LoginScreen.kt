package com.raf.mobiletaskcodeidtest.auth.presentation.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.raf.mobiletaskcodeidtest.auth.presentation.viewmodel.AuthViewModel
import com.raf.mobiletaskcodeidtest.core.presentation.component.SettingsDialog
import com.raf.mobiletaskcodeidtest.core.presentation.component.SettingsIconButton

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.LoginScreen(
    animatedContentScope: AnimatedContentScope,
    viewModel: AuthViewModel = hiltViewModel(),
    onNavigateToRegister: () -> Unit = {},
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val scrollState = rememberScrollState()
    var showSettingsDialog by rememberSaveable {
        mutableStateOf(false)
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Login",
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center
                    )
                },
                scrollBehavior = scrollBehavior,
                actions = {
                    SettingsIconButton(
                        visible = !showSettingsDialog,
                        onClick = { showSettingsDialog = true }
                    )
                }
            )
        },
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .animateContentSize()
        ) {
            OutlinedTextField(
                value = viewModel.email,
                onValueChange = {
                    viewModel.onLoginInputChange(email = it)
                },
                label = {
                    Text(text = "Email")
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Email
                ),
                keyboardActions = KeyboardActions {
                    this.defaultKeyboardAction(
                        imeAction = ImeAction.Next
                    )
                },
                singleLine = true,
                isError = viewModel.isEmailError,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = "Email"
                    )
                },
                trailingIcon = {
                    AnimatedVisibility(
                        visible = !viewModel.email.isBlank(),
                        enter = scaleIn(),
                        exit = scaleOut()
                    ) {
                        IconButton(
                            onClick = {
                                viewModel.onLoginInputChange(email = "")
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear"
                            )
                        }
                    }
                },
                modifier = Modifier
                    .sharedElement(
                        sharedContentState = rememberSharedContentState("email-key"),
                        animatedVisibilityScope = animatedContentScope
                    )
                    .fillMaxWidth()
                    .widthIn(max = 220.dp)
            )
            OutlinedTextField(
                value = viewModel.password,
                onValueChange = {
                    viewModel.onLoginInputChange(password = it)
                },
                label = {
                    Text(text = "Password")
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Password
                ),
                keyboardActions = KeyboardActions {
                    this.defaultKeyboardAction(
                        imeAction = ImeAction.Done
                    )
                },
                visualTransformation = if (viewModel.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                singleLine = true,
                isError = viewModel.isPasswordError,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Password,
                        contentDescription = "Password"
                    )
                },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            viewModel.onLoginInputChange(showPassword = !viewModel.isPasswordVisible)
                        }
                    ) {
                        Icon(
                            imageVector = if (viewModel.isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = "Show/Hide Password"
                        )
                    }
                },
                modifier = Modifier
                    .sharedElement(
                        sharedContentState = rememberSharedContentState("password-key"),
                        animatedVisibilityScope = animatedContentScope
                    )
                    .fillMaxWidth()
                    .widthIn(max = 220.dp)
            )
            uiState.errorMessage?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    viewModel.login()
                },
                enabled = !uiState.isLoading,
                modifier = Modifier
                    .sharedElement(
                        sharedContentState = rememberSharedContentState("auth-button-key"),
                        animatedVisibilityScope = animatedContentScope
                    )
                    .fillMaxWidth()
                    .widthIn(max = 220.dp)
            ) {
                AnimatedContent(
                    targetState = uiState.isLoading,
                    modifier = Modifier
                ) { targetState ->
                    if (targetState) {
                        CircularProgressIndicator()
                    } else {
                        Text(
                            text = "Login",
                            style = MaterialTheme.typography.titleMedium,
                        )
                    }
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(
                    16.dp,
                    Alignment.CenterHorizontally
                ),
                modifier = Modifier
                    .sharedElement(
                        sharedContentState = rememberSharedContentState("or-key"),
                        animatedVisibilityScope = animatedContentScope
                    )
                    .fillMaxWidth()
            ) {
                HorizontalDivider(modifier = Modifier.weight(1f))
                Text(
                    text = "OR",
                    style = MaterialTheme.typography.titleMedium,
                )
                HorizontalDivider(modifier = Modifier.weight(1f))
            }
            TextButton(
                onClick = {
                    onNavigateToRegister.invoke()
                    viewModel.resetState()
                },
                modifier = Modifier
                    .sharedElement(
                        sharedContentState = rememberSharedContentState("navigation-key"),
                        animatedVisibilityScope = animatedContentScope
                    )
            ) {
                Text(
                    text = "Register",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleMedium,
                )
            }
        }
    }
    SettingsDialog(
        isShown = showSettingsDialog,
        onDismissRequest = { showSettingsDialog = false },
        currentAppSettings = uiState.appSettings,
        onAppSettingsChange = { appSettings ->
            viewModel.setAppSettings(appSettings)
        }
    )
}