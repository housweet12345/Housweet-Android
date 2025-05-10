package com.housweet.presentation.ui.startPage.accessRoomPage.createRoomScreen

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class CreateRoomViewModel @Inject constructor(): ViewModel() {
    private val _uiState = MutableStateFlow<CreateRoomUiState>(CreateRoomUiState.IDlE)
    val uiState = _uiState.asStateFlow()

    private fun error() {
        _uiState.value = CreateRoomUiState.Error
    }
    private fun isLoading() {
        _uiState.value = CreateRoomUiState.IsLoading
    }
}