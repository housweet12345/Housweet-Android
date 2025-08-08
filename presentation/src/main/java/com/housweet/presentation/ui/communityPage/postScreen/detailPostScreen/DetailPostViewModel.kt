package com.housweet.presentation.ui.communityPage.postScreen.detailPostScreen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.housweet.domain.model.RoomPostDetailDataModel
import com.housweet.domain.usecase.UseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailPostViewModel @Inject constructor(
    private val useCases: UseCases,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    private val _uiState = MutableStateFlow<DetailPostState>(DetailPostState.Idle)
    val uiState: StateFlow<DetailPostState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<DetailPostEvent>()
    val event: SharedFlow<DetailPostEvent> = _event.asSharedFlow()

    private val _roomPostDetail = MutableStateFlow(RoomPostDetailDataModel())
    val roomPostDetail: StateFlow<RoomPostDetailDataModel> = _roomPostDetail.asStateFlow()

    init {
        val postId = savedStateHandle.get<Int>("postId")
        viewModelScope.launch {
            postId?.let { getRoomPostDetail(it) }
        }
    }

    private fun getRoomPostDetail(postId: Int) {
        viewModelScope.launch {
            useCases.getRoomPostDetailUseCase(postId).collect { result ->
                result.onSuccess {
                    _roomPostDetail.value = it
                }

                result.onFailure {
                    _event.emit(DetailPostEvent.Error)
                }
            }
        }
    }
}