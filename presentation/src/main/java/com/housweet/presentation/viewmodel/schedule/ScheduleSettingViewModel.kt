package com.housweet.presentation.viewmodel.schedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.housweet.presentation.model.schedule.TaskInfo
import com.housweet.presentation.ui.schedule.state.TaskSettingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScheduleSettingViewModel @Inject constructor(
) : ViewModel() {
    
    private val _taskSettingState: MutableStateFlow<TaskSettingState> = MutableStateFlow(TaskSettingState.Loading)
    val taskSettingState: StateFlow<TaskSettingState> = _taskSettingState.asStateFlow()

    private var currentTaskId: Int? = null

    fun loadTask(taskId: Int) {
        currentTaskId = taskId
        viewModelScope.launch {
            _taskSettingState.value = TaskSettingState.Loading
            
            try {
                // TODO: UseCase를 통해 실제 데이터를 가져오도록 수정 필요
                val taskInfo = TaskInfo(
                    id = taskId,
                    title = "청소기 돌리기",
                    selectedDayOfWeek = "금",
                    recurringOption = "1주마다 반복",
                    dateRange = "",
                    isScheduleMode = false,
                    isNotificationEnabled = true,
                    memo = "",
                    isCompleted = false
                )
                
                _taskSettingState.value = TaskSettingState.Success(taskInfo)
            } catch (e: Exception) {
                _taskSettingState.value = TaskSettingState.Error(
                    e.message ?: "할일 정보 로딩에 실패했습니다"
                )
            }
        }
    }

    fun loadTaskByTitle(title: String) {
        viewModelScope.launch {
            _taskSettingState.value = TaskSettingState.Loading
            
            try {
                // TODO: UseCase를 통해 실제 데이터를 가져오도록 수정 필요
                val taskInfo = TaskInfo(
                    id = 1,
                    title = title,
                    selectedDayOfWeek = "금",
                    recurringOption = "1주마다 반복",
                    dateRange = "",
                    isScheduleMode = false,
                    isNotificationEnabled = true,
                    memo = "",
                    isCompleted = false
                )
                
                _taskSettingState.value = TaskSettingState.Success(taskInfo)
            } catch (e: Exception) {
                _taskSettingState.value = TaskSettingState.Error(
                    e.message ?: "할일 정보 로딩에 실패했습니다"
                )
            }
        }
    }

    fun initializeNewTask() {
        currentTaskId = null
        _taskSettingState.value = TaskSettingState.Success(TaskInfo())
    }

    fun saveTask() {
        val currentState = _taskSettingState.value
        if (currentState is TaskSettingState.Success) {
            viewModelScope.launch {
                try {
                    // TODO: UseCase를 통해 실제 저장 로직 구현
                    if (currentTaskId != null) {
                        // 기존 할일 업데이트
                        // updateTaskUseCase(currentState.taskInfo)
                    } else {
                        // 새 할일 생성
                        // createTaskUseCase(currentState.taskInfo)
                    }
                    
                    _taskSettingState.value = TaskSettingState.Saved
                } catch (e: Exception) {
                    _taskSettingState.value = TaskSettingState.Error(
                        e.message ?: "할일 저장에 실패했습니다"
                    )
                }
            }
        }
    }

    fun deleteTask() {
        currentTaskId?.let { taskId ->
            viewModelScope.launch {
                try {
                    // TODO: UseCase를 통해 실제 삭제 로직 구현
                    // deleteTaskUseCase(taskId)
                    
                    _taskSettingState.value = TaskSettingState.Deleted
                } catch (e: Exception) {
                    _taskSettingState.value = TaskSettingState.Error(
                        e.message ?: "할일 삭제에 실패했습니다"
                    )
                }
            }
        }
    }

    fun updateTaskInfo(taskInfo: TaskInfo) {
        val currentState = _taskSettingState.value
        if (currentState is TaskSettingState.Success) {
            _taskSettingState.value = TaskSettingState.Success(taskInfo)
        }
    }
}