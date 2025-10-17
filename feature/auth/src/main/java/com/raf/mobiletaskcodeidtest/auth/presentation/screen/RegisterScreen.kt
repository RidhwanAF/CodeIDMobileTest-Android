package com.raf.mobiletaskcodeidtest.auth.presentation.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.RegisterScreen(
    animatedContentScope: AnimatedContentScope,
    viewModel: AuthViewModel = hiltViewModel(),
    onNavigateToLogin: () -> Unit = {},
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val scrollState = rememberScrollState()

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Register",
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onNavigateToLogin.invoke()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            OutlinedTextField(
                value = viewModel.email,
                onValueChange = {
                    viewModel.onRegisterInputChange(email = it)
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
                supportingText = {
                    AnimatedVisibility(
                        visible = viewModel.emailErrorMessage.isNotBlank()
                    ) {
                        Text(
                            text = viewModel.emailErrorMessage,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
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
                                viewModel.onRegisterInputChange(email = "")
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
                    viewModel.onRegisterInputChange(password = it)
                },
                label = {
                    Text(text = "Password")
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Password
                ),
                keyboardActions = KeyboardActions {
                    this.defaultKeyboardAction(
                        imeAction = ImeAction.Next
                    )
                },
                visualTransformation = if (viewModel.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                singleLine = true,
                isError = viewModel.isPasswordError,
                supportingText = {
                    AnimatedVisibility(
                        visible = viewModel.passwordErrorMessage.isNotBlank()
                    ) {
                        Text(
                            text = viewModel.passwordErrorMessage,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Password,
                        contentDescription = "Password"
                    )
                },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            viewModel.onRegisterInputChange(showPassword = !viewModel.isPasswordVisible)
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
            OutlinedTextField(
                value = viewModel.passwordConfirmation,
                onValueChange = {
                    viewModel.onRegisterInputChange(passwordConfirmation = it)
                },
                label = {
                    Text(text = "Password Confirmation")
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
                visualTransformation = if (viewModel.isPasswordConfirmationVisible) VisualTransformation.None else PasswordVisualTransformation(),
                singleLine = true,
                isError = viewModel.isPasswordConfirmationError,
                supportingText = {
                    AnimatedVisibility(
                        visible = viewModel.passwordConfirmationErrorMessage.isNotBlank()
                    ) {
                        Text(
                            text = viewModel.passwordConfirmationErrorMessage,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Password,
                        contentDescription = "Password Confirmation"
                    )
                },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            viewModel.onRegisterInputChange(showPasswordConfirmation = !viewModel.isPasswordConfirmationVisible)
                        }
                    ) {
                        Icon(
                            imageVector = if (viewModel.isPasswordConfirmationVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = "Show/Hide Password Confirmation"
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 220.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    viewModel.register()
                },
                enabled = !uiState.isLoading && !viewModel.isEmailError && !viewModel.isPasswordError &&
                        !viewModel.isPasswordConfirmationError && viewModel.email.isNotBlank() &&
                        viewModel.password.isNotBlank() && viewModel.passwordConfirmation.isNotBlank(),
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
                            text = "Register",
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
                    onNavigateToLogin.invoke()
                },
                modifier = Modifier
                    .sharedElement(
                        sharedContentState = rememberSharedContentState("navigation-key"),
                        animatedVisibilityScope = animatedContentScope
                    )
            ) {
                Text(
                    text = "Login",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleMedium,
                )
            }
        }
    }
}