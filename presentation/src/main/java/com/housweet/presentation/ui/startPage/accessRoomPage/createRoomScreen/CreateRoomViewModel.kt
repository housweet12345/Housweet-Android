package com.housweet.presentation.ui.startPage.accessRoomPage.createRoomScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateRoomViewModel @Inject constructor(): ViewModel() {
    private val _uiState = MutableStateFlow<CreateRoomState>(CreateRoomState.Idle)
    val uiState = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<CreateRoomEvent>()
    val event = _event.asSharedFlow()

    private fun error() {
        viewModelScope.launch {
            _event.emit(CreateRoomEvent.Error)
        }
    }
    private fun isLoading() {
        _uiState.value = CreateRoomState.IsLoading
    }
}