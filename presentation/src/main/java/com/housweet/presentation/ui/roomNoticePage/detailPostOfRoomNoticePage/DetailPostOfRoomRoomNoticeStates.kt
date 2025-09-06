package com.housweet.presentation.ui.roomNoticePage.detailPostOfRoomNoticePage

sealed interface DetailPostOfRoomNoticeState {
    data object Idle : DetailPostOfRoomNoticeState
    data object Loading : DetailPostOfRoomNoticeState
}

sealed interface DetailPostOfRoomNoticeEvent {
    data class Error(val message: String): DetailPostOfRoomNoticeEvent
}