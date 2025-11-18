package com.housweet.presentation.viewmodel.mypage

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.housweet.domain.model.NotificationModel
import com.housweet.domain.repository.NotificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface NotificationUiState {
    object Loading : NotificationUiState
    data class Success(val notifications: List<NotificationModel>) : NotificationUiState
    data class Error(val message: String) : NotificationUiState
}

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val repository: NotificationRepository
): ViewModel() {

    private val _uiState = MutableStateFlow<NotificationUiState>(NotificationUiState.Loading)
    val uiState: StateFlow<NotificationUiState> = _uiState.asStateFlow()

    fun fetchNotifications() {
        viewModelScope.launch {
            _uiState.value = NotificationUiState.Loading
            try {
                val notifications = repository.getNotifications()
                _uiState.value = NotificationUiState.Success(notifications)
            } catch (e: Exception) {
                Log.e("NotificationViewModel", "Failed to fetch notifications", e)
                _uiState.value = NotificationUiState.Error(
                    e.message ?: "알림을 불러오는데 실패했습니다"
                )
            }
        }
    }
}