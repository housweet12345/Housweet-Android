package com.housweet.presentation.ui.startPage.accessRoomPage.createRoomScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.housweet.domain.usecase.UseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateRoomViewModel @Inject constructor(
    private val useCases: UseCases
): ViewModel() {
    private val _uiState = MutableStateFlow<CreateRoomState>(CreateRoomState.Idle)
    val uiState = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<CreateRoomEvent>()
    val event = _event.asSharedFlow()

    fun createRoom(name: String) {
        isLoading()
        viewModelScope.launch {
            useCases.createRoomUseCase(name).collect {
                it.onSuccess { success ->
                    if (success) success() else error()
                }.onFailure {
                    error()
                }
            }
        }
    }

    private fun success() {
        viewModelScope.launch {
            _event.emit(CreateRoomEvent.Success)
        }
    }

    private fun error() {
        viewModelScope.launch {
            isIdle()
            _event.emit(CreateRoomEvent.Error)
        }
    }

    private fun isIdle() {
        _uiState.value = CreateRoomState.Idle
    }

    private fun isLoading() {
        _uiState.value = CreateRoomState.IsLoading
    }
}