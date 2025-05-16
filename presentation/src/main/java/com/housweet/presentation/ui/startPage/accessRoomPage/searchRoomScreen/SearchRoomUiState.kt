package com.housweet.presentation.ui.startPage.accessRoomPage.searchRoomScreen

sealed interface SearchRoomUiState {
    data object Idle : SearchRoomUiState
    data object Error : SearchRoomUiState
    data object IsLoading: SearchRoomUiState
}