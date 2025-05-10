package com.housweet.presentation.ui.startPage.accessRoomPage.createRoomScreen

sealed interface CreateRoomUiState {
    data object IDlE : CreateRoomUiState
    data object Error : CreateRoomUiState
    data object IsLoading: CreateRoomUiState
}