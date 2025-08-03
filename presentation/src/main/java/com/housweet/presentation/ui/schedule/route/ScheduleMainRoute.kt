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
import androidx.navigation.NavController
import com.housweet.presentation.ui.schedule.screen.ScheduleMainScreen
import com.housweet.presentation.ui.schedule.state.ScheduleState
import com.housweet.presentation.viewmodel.schedule.ScheduleViewModel

@Composable
fun ScheduleMainRoute(
    viewModel: ScheduleViewModel = hiltViewModel(),
    onMenuClick: () -> Unit = {},
    onAddClick: () -> Unit = {},
    navController: NavController
) {
    val state = viewModel.scheduleState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadSchedules()
    }

    when (val uiState = state.value) {
        is ScheduleState.Success -> {
            ScheduleMainScreen(
                onMenuClick = onMenuClick,
                onAddClick = onAddClick,
                scheduleInfo = uiState.schedules.firstOrNull(),
                allTasks = uiState.allTasks,
                currentMonth = viewModel.currentMonth.collectAsStateWithLifecycle().value,
                onDateSelected = { date ->
                    viewModel.updateSelectedDate(date)
                },
                onMonthChanged = { month ->
                    viewModel.updateCurrentMonth(month)
                },
                navController = navController
            )
        }
        is ScheduleState.Loading -> {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
        is ScheduleState.Error -> {
            // Error state can be handled here
            Box(modifier = Modifier.fillMaxSize()) {
                androidx.compose.material3.Text(
                    text = "오류가 발생했습니다: ${uiState.message}",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}