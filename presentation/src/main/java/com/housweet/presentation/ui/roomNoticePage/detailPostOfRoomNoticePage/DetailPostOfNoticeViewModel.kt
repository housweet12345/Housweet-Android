package com.housweet.presentation.ui.roomNoticePage.detailPostOfRoomNoticePage

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
class DetailPostOfNoticeViewModel @Inject constructor(

): ViewModel() {
    private val _uiState = MutableStateFlow<DetailPostOfRoomNoticeState>(DetailPostOfRoomNoticeState.Idle)
    val uiState: StateFlow<DetailPostOfRoomNoticeState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<DetailPostOfRoomNoticeEvent>()
    val event: SharedFlow<DetailPostOfRoomNoticeEvent> = _event.asSharedFlow()
}