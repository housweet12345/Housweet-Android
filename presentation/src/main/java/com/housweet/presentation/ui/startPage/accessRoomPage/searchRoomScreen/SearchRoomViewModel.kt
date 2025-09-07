package com.housweet.presentation.ui.startPage.accessRoomPage.searchRoomScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.housweet.domain.usecase.start.AccessRoomWithInviteCodeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchRoomViewModel @Inject constructor(
    private val accessRoomWithInviteCodeUseCase: AccessRoomWithInviteCodeUseCase
): ViewModel() {
    private val _uiState = MutableStateFlow<SearchRoomState>(SearchRoomState.Idle)
    val uiState = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<SearchRoomEvent>()
    val event = _event.asSharedFlow()

    fun accessRoomWithInviteCode(inviteCode: String) {
        isLoading()
        viewModelScope.launch {
            accessRoomWithInviteCodeUseCase(inviteCode).collect {
                it.onSuccess { success ->
                    if (success) success() else error()
                }.onFailure {
                    error()
                }
            }
        }
    }

    private suspend fun success() {
        _event.emit(SearchRoomEvent.Success)
    }

    private suspend fun error() {
        isIdle()
        _event.emit(SearchRoomEvent.Error)
    }

    private fun isIdle() {
        _uiState.value = SearchRoomState.Idle
    }

    private fun isLoading() {
        _uiState.value = SearchRoomState.IsLoading
    }
}