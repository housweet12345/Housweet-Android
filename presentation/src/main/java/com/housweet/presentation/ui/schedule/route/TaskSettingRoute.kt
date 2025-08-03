package com.housweet.presentation.ui.schedule.route

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.housweet.presentation.ui.schedule.screen.TaskSettingScreen
import com.housweet.presentation.ui.schedule.state.TaskSettingState
import com.housweet.presentation.model.schedule.TaskInfo
import com.housweet.presentation.viewmodel.schedule.ScheduleSettingViewModel

@Composable
fun TaskSettingRoute(
    taskTitle: String? = null,
    viewModel: ScheduleSettingViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {},
    onCompleteClick: () -> Unit = {}
) {
    val state = viewModel.taskSettingState.collectAsStateWithLifecycle()
    val isEditMode = taskTitle != null

    LaunchedEffect(taskTitle) {
        if (taskTitle != null) {
            viewModel.loadTaskByTitle(taskTitle)
        } else {
            viewModel.initializeNewTask()
        }
    }

    when (val uiState = state.value) {
        is TaskSettingState.Success -> {
            TaskSettingScreen(
                taskInfo = uiState.taskInfo,
                isEditMode = isEditMode,
                onBackClick = onBackClick,
                onDeleteClick = {
                    viewModel.deleteTask()
                    onDeleteClick()
                },
                onCompleteClick = {
                    viewModel.saveTask()
                    onCompleteClick()
                },
                onTaskInfoUpdate = { updatedTaskInfo ->
                    viewModel.updateTaskInfo(updatedTaskInfo)
                }
            )
        }
        is TaskSettingState.Loading -> {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
        is TaskSettingState.Saved -> {
            LaunchedEffect(Unit) {
                onCompleteClick()
            }
        }
        is TaskSettingState.Deleted -> {
            LaunchedEffect(Unit) {
                onDeleteClick()
            }
        }
        is TaskSettingState.Error -> {
            // Show error screen with default TaskInfo
            TaskSettingScreen(
                taskInfo = TaskInfo(),
                isEditMode = isEditMode,
                onBackClick = onBackClick,
                onDeleteClick = {
                    viewModel.deleteTask()
                    onDeleteClick()
                },
                onCompleteClick = {
                    viewModel.saveTask()
                    onCompleteClick()
                },
                onTaskInfoUpdate = { updatedTaskInfo ->
                    viewModel.updateTaskInfo(updatedTaskInfo)
                }
            )
        }
    }
}