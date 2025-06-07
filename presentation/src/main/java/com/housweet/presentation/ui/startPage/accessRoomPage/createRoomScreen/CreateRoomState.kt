package com.housweet.presentation.ui.startPage.accessRoomPage.createRoomScreen

sealed interface CreateRoomState {
    data object Idle : CreateRoomState
    data object IsLoading: CreateRoomState
}

sealed interface CreateRoomEvent {
    data object Success : CreateRoomEvent
    data object Error : CreateRoomEvent
}