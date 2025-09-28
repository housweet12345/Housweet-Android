package com.housweet.presentation.ui.roomNoticePage.roomNoticePostsPage

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class RoomNoticePostsViewModel @Inject constructor(

): ViewModel() {
    private val _uiState = MutableStateFlow<RoomNoticePostsStates>(RoomNoticePostsStates.Idle)
    val uiState: StateFlow<RoomNoticePostsStates> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<RoomNoticePostsEvent>()
    val event: SharedFlow<RoomNoticePostsEvent> = _event.asSharedFlow()
}