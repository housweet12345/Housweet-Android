package com.housweet.presentation.ui.roomNoticePage.writePostOfRoomNoticePage

sealed interface WritePostOfNoticeState {
    data object Idle : WritePostOfNoticeState
    data object Loading : WritePostOfNoticeState
}

sealed interface WritePostOfNoticeEvent {
    data object Error: WritePostOfNoticeEvent
    data object CurseFiltering: WritePostOfNoticeEvent
}