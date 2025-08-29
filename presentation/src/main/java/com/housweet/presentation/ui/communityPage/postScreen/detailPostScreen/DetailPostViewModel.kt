package com.housweet.presentation.ui.communityPage.postScreen.detailPostScreen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.housweet.domain.model.RoomPostDetailDataModel
import com.housweet.domain.usecase.UseCases
import com.housweet.domain.usecase.auth.GetCurrentUserIdUseCase
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
    private val currentUserIdUseCase: GetCurrentUserIdUseCase,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    private val _uiState = MutableStateFlow<DetailPostState>(DetailPostState.Idle)
    val uiState: StateFlow<DetailPostState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<DetailPostEvent>()
    val event: SharedFlow<DetailPostEvent> = _event.asSharedFlow()

    private val _roomPostDetail = MutableStateFlow(RoomPostDetailDataModel())
    val roomPostDetail: StateFlow<RoomPostDetailDataModel> = _roomPostDetail.asStateFlow()

    var originalBookMarkState: Boolean = false
    var currentUserId: Int? = null

    private var postId = savedStateHandle.get<Int>("postId")
    var lastRegion = savedStateHandle.get<String>("lastRegion") ?: ""

    init {
        viewModelScope.launch {
            currentUserId = currentUserIdUseCase()
            postId?.let { getRoomPostDetail(it) }
        }
    }

    private fun getRoomPostDetail(postId: Int) {
        viewModelScope.launch {
            useCases.getRoomPostDetailUseCase(postId).collect { result ->
                result.onSuccess {
                    _roomPostDetail.value = it
                    originalBookMarkState = it.isBookmarked
                }

                result.onFailure {
                    _event.emit(DetailPostEvent.Error)
                }
            }
        }
    }

    fun toggleLike() {
        val originalPost = _roomPostDetail.value
        val updatedRoomPostDetail = _roomPostDetail.value.copy(
            isBookmarked = !originalPost.isBookmarked
        )

        _roomPostDetail.value = updatedRoomPostDetail

        viewModelScope.launch {
            if (!originalPost.isBookmarked) {
                useCases.clickBookMarkUseCase(originalPost.id).collect { result ->
                    result.onFailure {
                        rollbackBookMark(originalPost)
                        _event.emit(DetailPostEvent.Error)
                    }
                }
            } else {
                useCases.unClickBookMarkUseCase(originalPost.id).collect { result ->
                    result.onFailure {
                        rollbackBookMark(originalPost)
                        _event.emit(DetailPostEvent.Error)
                    }
                }
            }
        }
    }

    private fun rollbackBookMark(originalPost: RoomPostDetailDataModel) {
        _roomPostDetail.value = originalPost
    }

    fun reportRoom() {
        viewModelScope.launch {
            useCases.reportRoomPostUseCase(_roomPostDetail.value.id).collect { result ->
                result.onSuccess {
                    _event.emit(DetailPostEvent.ReportRoom("신고 접수가 완료되었습니다."))
                }

                result.onFailure {
                    _event.emit(DetailPostEvent.ReportRoom("신고 접수에 실패했습니다."))
                }
            }
        }
    }
}