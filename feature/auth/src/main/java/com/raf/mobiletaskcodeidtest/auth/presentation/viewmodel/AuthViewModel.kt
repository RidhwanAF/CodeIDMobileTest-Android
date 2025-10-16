package com.raf.mobiletaskcodeidtest.auth.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.raf.mobiletaskcodeidtest.auth.domain.usecase.SaveTokenSessionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val saveTokenSessionUseCase: SaveTokenSessionUseCase
) : ViewModel() {

}