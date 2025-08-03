package com.housweet.presentation.viewmodel.schedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.housweet.presentation.model.schedule.TaskType
import com.housweet.presentation.model.schedule.OwnerTaskGroup
import com.housweet.presentation.model.schedule.ScheduleInfo
import com.housweet.presentation.ui.schedule.state.ScheduleState
import com.housweet.presentation.model.schedule.TaskItem
import java.time.LocalDate
import java.time.YearMonth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
) : ViewModel() {
    
    private val _scheduleState: MutableStateFlow<ScheduleState> = MutableStateFlow(ScheduleState.Loading)
    val scheduleState: StateFlow<ScheduleState> = _scheduleState.asStateFlow()
    
    private val _currentMonth: MutableStateFlow<YearMonth> = MutableStateFlow(YearMonth.now())
    val currentMonth: StateFlow<YearMonth> = _currentMonth.asStateFlow()
    
    private val _selectedDate: MutableStateFlow<LocalDate> = MutableStateFlow(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> = _selectedDate.asStateFlow()

    fun loadSchedules() {
        viewModelScope.launch {
            _scheduleState.value = ScheduleState.Loading
            
            try {
                // TODO: UseCase를 통해 실제 데이터를 가져오도록 수정 필요
                val currentMonth = YearMonth.now()
                val scheduleInfo = ScheduleInfo(
                    id = 1,
                    title = "03.01.TUE",
                    date = "2024-03-01",
                    ownerTaskGroups = listOf(
                        OwnerTaskGroup(
                            ownerName = "김지안님",
                            isMine = true,
                            tasks = listOf(
                                TaskItem(1, TaskType.SCHEDULE, "청소기 돌리기", false, currentMonth.atDay(15)),
                                TaskItem(2, TaskType.TODO, "설거지하기", true, currentMonth.atDay(20)),
                                TaskItem(3, TaskType.TODO, "분리수거하기", true, currentMonth.atDay(20))
                            )
                        ),
                        OwnerTaskGroup(
                            ownerName = "이민철님",
                            isMine = false,
                            tasks = listOf(
                                TaskItem(4, TaskType.SCHEDULE, "빨래하기", false, currentMonth.atDay(27)),
                                TaskItem(5, TaskType.TODO, "방 청소하기", true, currentMonth.atDay(28)),
                                TaskItem(6, TaskType.TODO, "화장실 청소하기", true, currentMonth.atDay(28))
                            )
                        )
                    )
                )
                
                val schedules = listOf(scheduleInfo)
                val allTasks = schedules.flatMap { schedule ->
                    schedule.ownerTaskGroups.flatMap { group ->
                        group.tasks
                    }
                }
                
                _scheduleState.value = ScheduleState.Success(schedules, allTasks)
            } catch (e: Exception) {
                _scheduleState.value = ScheduleState.Error(
                    e.message ?: "일정 데이터 로딩에 실패했습니다"
                )
            }
        }
    }
    
    fun updateSelectedDate(date: LocalDate) {
        _selectedDate.value = date
    }
    
    fun updateCurrentMonth(month: YearMonth) {
        _currentMonth.value = month
    }
    
}