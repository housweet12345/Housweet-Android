package com.housweet.presentation.ui.roomNoticePage.writeRuleOfRoomPage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WriteRuleOfRoomViewModel @Inject constructor(

): ViewModel() {
    private val _uiState = MutableStateFlow<WriteRuleOfRoomUiState>(WriteRuleOfRoomUiState.Idle)
    val uiState: StateFlow<WriteRuleOfRoomUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<WriteRuleOfRoomEvent>()
    val event: SharedFlow<WriteRuleOfRoomEvent> = _event.asSharedFlow()

    fun curseError() {
        viewModelScope.launch {
            _event.emit(WriteRuleOfRoomEvent.CurseFiltering)
        }
    }
}