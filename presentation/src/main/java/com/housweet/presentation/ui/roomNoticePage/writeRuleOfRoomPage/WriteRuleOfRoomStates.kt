package com.housweet.presentation.ui.roomNoticePage.writeRuleOfRoomPage

sealed interface WriteRuleOfRoomUiState {
    data object Idle : WriteRuleOfRoomUiState
    data object Loading : WriteRuleOfRoomUiState
}

sealed interface WriteRuleOfRoomEvent {
    data object Error: WriteRuleOfRoomEvent
    data object CurseFiltering: WriteRuleOfRoomEvent
}