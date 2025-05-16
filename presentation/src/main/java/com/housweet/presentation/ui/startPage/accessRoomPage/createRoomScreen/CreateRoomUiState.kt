package com.housweet.presentation.ui.startPage.accessRoomPage.createRoomScreen

sealed interface CreateRoomUiState {
    data object Idle : CreateRoomUiState
    data object Error : CreateRoomUiState
    data object IsLoading: CreateRoomUiState
}