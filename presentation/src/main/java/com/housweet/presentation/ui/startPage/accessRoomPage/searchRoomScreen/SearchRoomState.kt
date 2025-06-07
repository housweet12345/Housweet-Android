package com.housweet.presentation.ui.startPage.accessRoomPage.searchRoomScreen

sealed interface SearchRoomState {
    data object Idle : SearchRoomState
    data object IsLoading: SearchRoomState
}

sealed interface SearchRoomEvent {
    data object Success : SearchRoomEvent
    data object Error : SearchRoomEvent
}