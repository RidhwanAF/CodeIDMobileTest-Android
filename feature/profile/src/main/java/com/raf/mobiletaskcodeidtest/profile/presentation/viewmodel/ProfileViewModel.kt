package com.raf.mobiletaskcodeidtest.profile.presentation.viewmodel

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raf.mobiletaskcodeidtest.core.domain.model.AppSettings
import com.raf.mobiletaskcodeidtest.core.domain.usecase.GetAccountInfoUseCase
import com.raf.mobiletaskcodeidtest.core.domain.usecase.GetAppSettingsUseCase
import com.raf.mobiletaskcodeidtest.core.domain.usecase.GetTokenSessionUseCase
import com.raf.mobiletaskcodeidtest.core.domain.usecase.LogoutUseCase
import com.raf.mobiletaskcodeidtest.core.domain.usecase.SetAppSettingsUseCase
import com.raf.mobiletaskcodeidtest.profile.domain.model.Profile
import com.raf.mobiletaskcodeidtest.profile.domain.usecase.GetProfileUseCase
import com.raf.mobiletaskcodeidtest.profile.domain.usecase.UpdateProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val application: Application,
    private val getAppSettingsUseCase: GetAppSettingsUseCase,
    private val setAppSettingsUseCase: SetAppSettingsUseCase,
    private val getTokenSessionUseCase: GetTokenSessionUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val getProfileUseCase: GetProfileUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase,
    private val getAccountInfoUseCase: GetAccountInfoUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getAppSettings()
        getProfile()
    }

    /**
     * Create Profile
     */
    var profilePictureInput by mutableStateOf<Uri?>(null)
        private set

    var nameInput by mutableStateOf("")
        private set

    fun onProfilePictureInputChange(newValue: Uri?) {
        profilePictureInput = newValue
    }

    fun onNameInputChange(newValue: String) {
        nameInput = newValue
    }

    fun resetInputs() {
        profilePictureInput = null
        nameInput = ""
    }

    fun registerProfile(onFinish: () -> Unit) {
        viewModelScope.launch {
            val userId = getTokenSessionUseCase.invoke().first()
            if (userId == null) {
                showErrorMessage("User not found")
                Log.e(TAG, "User not found")
                return@launch
            }
            val secureUriString = withContext(Dispatchers.IO) {
                try {
                    if (profilePictureInput == null) return@withContext null
                    val inputStream =
                        application.contentResolver.openInputStream(profilePictureInput!!)
                            ?: return@withContext null

                    val file = File(
                        application.filesDir,
                        "profile_picture_${System.currentTimeMillis()}.jpg"
                    )

                    FileOutputStream(file).use { outputStream ->
                        inputStream.use { input ->
                            input.copyTo(outputStream)
                        }
                    }
                    val contentUri: Uri = FileProvider.getUriForFile(
                        application,
                        FILE_PROVIDER_AUTHORITY,
                        file
                    )
                    contentUri.toString()
                } catch (e: Exception) {
                    Log.e(TAG, "Failed to save and generate secure URI for image", e)
                    null
                }
            }

            val newProfile = Profile(
                userId = userId,
                email = "",
                name = nameInput,
                picturePath = secureUriString ?: ""
            )
            updateProfileUseCase.invoke(newProfile)
            onFinish()
        }
    }

    /**
     * Profile
     */
    private fun getProfile() {
        viewModelScope.launch {
            val userId = getTokenSessionUseCase().first() ?: return@launch
            val email = getAccountInfoUseCase(userId) ?: ""

            getProfileUseCase(userId).fold(
                onSuccess = { profile ->
                    _uiState.update {
                        it.copy(profile = profile.copy(email = email))
                    }
                    nameInput = profile.name
                    Log.d(TAG, "getProfile: $profile")
                },
                onFailure = { throwable ->
                    showErrorMessage(throwable.localizedMessage ?: "Unknown error")
                    Log.e(TAG, "getProfile: ${throwable.message}", throwable)
                }
            )
        }
    }

    fun onEditProfilePicture(uri: Uri) {
        viewModelScope.launch {
            val imagePath = withContext(Dispatchers.IO) {
                try {
                    val inputStream =
                        application.contentResolver.openInputStream(uri)
                            ?: return@withContext null

                    val file = File(
                        application.filesDir,
                        "profile_picture_${System.currentTimeMillis()}.jpg"
                    )

                    FileOutputStream(file).use { outputStream ->
                        inputStream.use { input ->
                            input.copyTo(outputStream)
                        }
                    }
                    val contentUri: Uri = FileProvider.getUriForFile(
                        application,
                        FILE_PROVIDER_AUTHORITY,
                        file
                    )
                    contentUri.toString()
                } catch (e: Exception) {
                    Log.e(TAG, "Failed to save and generate secure URI for image", e)
                    null
                }
            }

            if (imagePath == null) {
                showErrorMessage("Failed to save image")
                return@launch
            }

            val newProfile = _uiState.value.profile?.copy(
                picturePath = imagePath
            )
            if (newProfile == null) return@launch
            updateProfileUseCase(newProfile)
            getProfile()
        }
    }

    fun onEditProfileNameSave(onFinish: () -> Unit) {
        viewModelScope.launch {
            val newProfile = _uiState.value.profile?.copy(
                name = nameInput
            )
            if (newProfile == null) return@launch
            updateProfileUseCase(newProfile)
            getProfile()
            onFinish()
        }
    }

    fun loadProfileImageBitmap(uri: Uri?): Bitmap? {
        if (uri == null) return null

        return try {
            application.contentResolver.openInputStream(uri)?.use { inputStream ->
                BitmapFactory.decodeStream(inputStream)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error loading bitmap from URI: $uri", e)
            null
        }
    }

    private fun showErrorMessage(message: String) {
        viewModelScope.launch {
            if (_uiState.value.errorMessage != null) return@launch
            _uiState.update { it.copy(errorMessage = message) }
            delay(2000)
            _uiState.update { it.copy(errorMessage = null) }
        }
    }

    /**
     * App Settings
     */
    private fun getAppSettings() {
        viewModelScope.launch {
            val appSettings = getAppSettingsUseCase().first()
            _uiState.update {
                it.copy(
                    appSettings = appSettings
                )
            }
        }
    }

    fun setAppSettings(appSettings: AppSettings) {
        viewModelScope.launch {
            setAppSettingsUseCase(appSettings)
            getAppSettings()
        }
    }

    /**
     * Logout
     */
    fun logout() {
        viewModelScope.launch {
            logoutUseCase.invoke()
        }
    }

    companion object {
        private const val TAG = "ProfileViewModel"
        private const val FILE_PROVIDER_AUTHORITY = "com.raf.mobiletaskcodeidtest.fileprovider"
    }
}