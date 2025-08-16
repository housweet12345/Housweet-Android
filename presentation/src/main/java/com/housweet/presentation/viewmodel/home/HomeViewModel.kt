package com.housweet.presentation.viewmodel.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.housweet.domain.usecase.home.GetRoomHomeUseCase
import com.housweet.presentation.ui.home.state.HomeState
import com.housweet.presentation.ui.home.state.MoodType
import com.housweet.presentation.ui.home.state.toHomeInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getRoomHomeUseCase: GetRoomHomeUseCase
) : ViewModel() {
    
    private val _homeState: MutableStateFlow<HomeState> = MutableStateFlow(HomeState.Loading)
    val homeState: StateFlow<HomeState> = _homeState.asStateFlow()

    init {
        loadHomeData()
    }

    fun loadHomeData() {
        viewModelScope.launch {
            _homeState.value = HomeState.Loading
            val result = getRoomHomeUseCase()

            result.onSuccess { roomHomeModel ->
                val homeInfo = roomHomeModel.toHomeInfo()
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
        // TODO: 사용자 기분 업데이트 로직 구현
        viewModelScope.launch {
            try {
                // API 호출로 기분 업데이트
                // updateMoodUseCase(moodType)
                loadHomeData() // 데이터 새로고침
            } catch (e: Exception) {
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