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
open class NoticeDetailViewModel @Inject constructor(
    private val repo: NoticeRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<NoticeDetailUiState>(NoticeDetailUiState.Loading)
    val uiState: StateFlow<NoticeDetailUiState> = _uiState

    fun load(id: Int) {
        viewModelScope.launch {
            runCatching { repo.getNotice(id) }
                .onSuccess { _uiState.value = NoticeDetailUiState.Success(it) }
                .onFailure { _uiState.value = NoticeDetailUiState.Error(it.message ?: "에러") }
        }
    }
}

sealed interface NoticeDetailUiState {
    data object Loading : NoticeDetailUiState
    data class Success(val notice: Notice) : NoticeDetailUiState
    data class Error(val message: String) : NoticeDetailUiState
}