package com.housweet.presentation.ui.mypage.deleteAccount

sealed interface DeleteAccountUiState {
    data object Idle : DeleteAccountUiState
    data object IsLoading: DeleteAccountUiState
}

sealed interface DeleteAccountEvent {
    data class IsBelongToRoom(val isBelongToRoom: Boolean) : DeleteAccountEvent
    data object DeleteAccountSuccess : DeleteAccountEvent
    data class Error(val message: String) : DeleteAccountEvent
}