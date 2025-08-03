package com.housweet.presentation.model.schedule

import java.time.LocalDate

enum class TaskType {
    TODO,      // 할 일
    SCHEDULE   // 일정
}

data class TaskItem(
    val id: Int,
    val taskType: TaskType,
    val taskName: String,
    val isCompleted: Boolean,
    val date: LocalDate
)

data class OwnerTaskGroup(
    val ownerName: String,
    val isMine: Boolean,
    val tasks: List<TaskItem>
)

data class ScheduleInfo(
    val id: Int,
    val title: String,
    val date: String,
    val ownerTaskGroups: List<OwnerTaskGroup>
)

data class TodoInfo(
    val id: Int,
    val title: String,
    val isCompleted: Boolean,
    val assignedUserId: Int,
    val type: TaskType = TaskType.TODO
)

data class EditableTodoInfo(
    val id: Int,
    val title: String,
    val isCompleted: Boolean,
    val isPast: Boolean = false
)

data class TaskInfo(
    val id: Int = 0,
    val title: String = "",
    val selectedDayOfWeek: String = "금",
    val recurringOption: String = "1주마다 반복",
    val dateRange: String = "",
    val isScheduleMode: Boolean = false,
    val isNotificationEnabled: Boolean = true,
    val memo: String = "",
    val isCompleted: Boolean = false
)