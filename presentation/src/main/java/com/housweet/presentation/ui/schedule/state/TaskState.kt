package com.housweet.presentation.ui.schedule.state

import com.housweet.presentation.model.schedule.ScheduleInfo
import com.housweet.presentation.model.schedule.TaskItem
import com.housweet.presentation.model.schedule.TaskInfo
import com.housweet.presentation.model.schedule.EditableTodoInfo

sealed interface ScheduleState {
    data object Loading : ScheduleState
    data class Success(
        val schedules: List<ScheduleInfo>,
        val allTasks: List<TaskItem>
    ) : ScheduleState
    data class Error(val message: String) : ScheduleState
}

sealed interface TaskSettingState {
    data object Loading : TaskSettingState
    data class Success(val taskInfo: TaskInfo) : TaskSettingState
    data object Saved : TaskSettingState
    data object Deleted : TaskSettingState
    data class Error(val message: String) : TaskSettingState
}

sealed interface MyTodoEditState {
    data object Loading : MyTodoEditState
    data class Success(val todos: List<EditableTodoInfo>) : MyTodoEditState
    data class Error(val message: String) : MyTodoEditState
}