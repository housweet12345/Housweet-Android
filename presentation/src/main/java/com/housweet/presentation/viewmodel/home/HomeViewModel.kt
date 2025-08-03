package com.housweet.presentation.viewmodel.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.housweet.presentation.ui.home.state.HomeState
import com.housweet.presentation.model.home.HomeInfo
import com.housweet.presentation.model.home.MoodType
import com.housweet.presentation.model.home.NoticeItem
import com.housweet.presentation.model.home.RoommateInfo
import com.housweet.presentation.model.schedule.TodoInfo
import com.housweet.presentation.model.schedule.TaskType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
) : ViewModel() {
    
    private val _homeState: MutableStateFlow<HomeState> = MutableStateFlow(HomeState.Loading)
    val homeState: StateFlow<HomeState> = _homeState.asStateFlow()

    init {
        loadHomeData()
    }

    fun loadHomeData() {
        viewModelScope.launch {
            _homeState.value = HomeState.Loading
            
            try {
                // TODO: UseCase를 통해 실제 데이터를 가져오도록 수정 필요
                val homeInfo = HomeInfo(
                    roomName = "곰돌이방",
                    daysLiving = 1,
                    notices = listOf(
                        NoticeItem(1, "23일 집에 일찍 돌아오기", "중요한 공지사항입니다.", "2024-01-23")
                    ),
                    roommates = listOf(
                        RoommateInfo(1, "김지안", "", MoodType.NORMAL),
                        RoommateInfo(2, "김지안", "", MoodType.NORMAL)
                    ),
                    todos = emptyList()
                )
                
                _homeState.value = HomeState.Success(homeInfo)
            } catch (e: Exception) {
                _homeState.value = HomeState.Error(
                    e.message ?: "홈 데이터 로딩에 실패했습니다"
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