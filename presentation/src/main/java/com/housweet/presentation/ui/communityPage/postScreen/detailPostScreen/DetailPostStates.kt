package com.housweet.presentation.ui.communityPage.postScreen.detailPostScreen

sealed interface DetailPostState {
    data object Idle : DetailPostState
    data object IsLoading : DetailPostState
}

sealed interface DetailPostEvent {
    data object Error: DetailPostEvent
    data object BookMarkError: DetailPostEvent
    data class ReportRoom(val message: String): DetailPostEvent
}