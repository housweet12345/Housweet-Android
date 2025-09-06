package com.housweet.presentation.ui.roomNoticePage.roomNoticePostsPage

sealed interface RoomNoticePostsStates {
    data object Idle : RoomNoticePostsStates
    data object Loading : RoomNoticePostsStates
}

sealed interface RoomNoticePostsEvent {
    data object Error: RoomNoticePostsEvent
}