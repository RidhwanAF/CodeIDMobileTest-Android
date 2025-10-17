package com.raf.mobiletaskcodeidtest.profile.presentation.viewmodel

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raf.mobiletaskcodeidtest.core.domain.model.AppSettings
import com.raf.mobiletaskcodeidtest.core.domain.usecase.GetAppSettingsUseCase
import com.raf.mobiletaskcodeidtest.core.domain.usecase.GetTokenSessionUseCase
import com.raf.mobiletaskcodeidtest.core.domain.usecase.LogoutUseCase
import com.raf.mobiletaskcodeidtest.core.domain.usecase.SetAppSettingsUseCase
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
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getAppSettings()
        getProfile()
    }

    private fun getProfile() {
        viewModelScope.launch {
            val userId = getTokenSessionUseCase().first() ?: return@launch
            getProfileUseCase(userId).fold(
                onSuccess = { profile ->
                    _uiState.update {
                        it.copy(
                            profile = profile
                        )
                    }
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
                    val inputStream = application.contentResolver.openInputStream(uri)
                    val file = File(application.filesDir, "profile_picture_${System.currentTimeMillis()}.jpg")
                    val outputStream = FileOutputStream(file)
                    inputStream?.use { input ->
                        outputStream.use { output ->
                            input.copyTo(output)
                        }
                    }
                    file.absolutePath
                } catch (e: Exception) {
                    Log.e(TAG, "Failed to save image", e)
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

    fun loadProfileImageBitmap(): Bitmap? {
        Log.d(TAG, "loadProfileImageBitmap: ${_uiState.value.profile?.picturePath}")
        if (_uiState.value.profile?.picturePath == null) return null
        val imagePath = _uiState.value.profile?.picturePath
        return BitmapFactory.decodeFile(imagePath)
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
    }
}