package com.housweet.presentation.viewmodel.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.housweet.domain.model.Notice
import com.housweet.domain.repository.NoticeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoticeViewModel @Inject constructor(
    private val repo: NoticeRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<NoticeUiState>(NoticeUiState.Loading)
    val uiState: StateFlow<NoticeUiState> = _uiState

    fun load() {
        viewModelScope.launch {
            runCatching { repo.getNotices() }
//                .onSuccess { _uiState.value = NoticeUiState.Success(it) }
//                .onFailure { _uiState.value = NoticeUiState.Error(it.message ?: "에러") }
                .onSuccess { list ->
                    android.util.Log.d("NoticeViewModel", "loaded notices size=${list.size}")
                    _uiState.value = NoticeUiState.Success(list)
                }
                .onFailure { e ->
                    android.util.Log.e("NoticeViewModel", "load error", e)
                    _uiState.value = NoticeUiState.Error(e.message ?: "알 수 없는 오류")
                }
        }
    }
}

sealed interface NoticeUiState {
    data object Loading : NoticeUiState
    data class Success(val notices: List<Notice>) : NoticeUiState
    data class Error(val message: String) : NoticeUiState
}