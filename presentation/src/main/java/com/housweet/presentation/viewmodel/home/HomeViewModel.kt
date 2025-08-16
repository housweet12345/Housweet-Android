package com.housweet.presentation.viewmodel.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.housweet.domain.usecase.auth.GetCurrentUserIdUseCase
import com.housweet.domain.usecase.home.GetRoomHomeUseCase
import com.housweet.domain.usecase.home.UpdateMoodUseCase
import com.housweet.presentation.ui.home.state.HomeState
import com.housweet.presentation.ui.home.state.MoodType
import com.housweet.presentation.ui.home.state.mapMoodTypeToString
import com.housweet.presentation.ui.home.state.toHomeInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getRoomHomeUseCase: GetRoomHomeUseCase,
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase,
    private val updateMoodUseCase: UpdateMoodUseCase
) : ViewModel() {
    
    private val _homeState: MutableStateFlow<HomeState> = MutableStateFlow(HomeState.Loading)
    val homeState: StateFlow<HomeState> = _homeState.asStateFlow()

    init {
        loadHomeData()
    }

    fun loadHomeData(showLoading: Boolean = true) {
        viewModelScope.launch {
            if (showLoading) {
                _homeState.value = HomeState.Loading
            }
            val result = getRoomHomeUseCase()

            result.onSuccess { roomHomeModel ->
                val currentUserId = getCurrentUserIdUseCase()
                val homeInfo = roomHomeModel.toHomeInfo(currentUserId)
                _homeState.value = HomeState.Success(homeInfo)
            }.onFailure { error ->
                _homeState.value = HomeState.Error(
                    error.message ?: "홈 정보를 불러오는데 실패했습니다"
                )
            }
        }
    }

    fun toggleTodoComplete(todoId: Int) {
        val currentState = _homeState.value
        if (currentState is HomeState.Success) {
            val updatedTodos = currentState.homeInfo.todos.map { todo ->
                if (todo.id == todoId) {
                    todo.copy(isCompleted = !todo.isCompleted)
                } else {
                    todo
                }
            }
            
            val updatedHomeInfo = currentState.homeInfo.copy(todos = updatedTodos)
            _homeState.value = HomeState.Success(updatedHomeInfo)
        }
    }

    fun updateMood(moodType: MoodType) {
        viewModelScope.launch {
            try {
                val currentState = _homeState.value
                if (currentState is HomeState.Success) {
                    val roomId = currentState.homeInfo.roomId
                    val feelingString = mapMoodTypeToString(moodType)
                    
                    val result = updateMoodUseCase(roomId, feelingString)
                    result.onSuccess {
                        // 기분 업데이트 성공 후 홈 데이터 새로고침 (Loading 없이)
                        loadHomeData(showLoading = false)
                    }.onFailure { error ->
                        // 실패 시에도 홈 데이터를 새로고침하여 원래 상태로 복원 (Loading 없이)
                        loadHomeData(showLoading = false)
                        _homeState.value = HomeState.Error(
                            error.message ?: "기분 업데이트에 실패했습니다"
                        )
                    }
                }
            } catch (e: Exception) {
                // 예외 발생 시에도 홈 데이터를 새로고침하여 원래 상태로 복원 (Loading 없이)
                loadHomeData(showLoading = false)
                _homeState.value = HomeState.Error(
                    e.message ?: "기분 업데이트에 실패했습니다"
                )
            }
        }
    }

    fun navigateToChat() {
        // TODO: 채팅 화면으로 이동 로직
    }

    fun navigateToNotification() {
        // TODO: 알림 화면으로 이동 로직
    }

    fun navigateToProfile() {
        // TODO: 프로필 화면으로 이동 로직
    }
}