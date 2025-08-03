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
import com.housweet.presentation.ui.schedule.screen.MyTodoEditScreen
import com.housweet.presentation.ui.schedule.state.MyTodoEditState
import com.housweet.presentation.model.schedule.EditableTodoInfo
import com.housweet.presentation.viewmodel.schedule.MyTodoEditViewModel

@Composable
fun MyTodoEditRoute(
    viewModel: MyTodoEditViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
    onTodoClick: (EditableTodoInfo) -> Unit = {},
    onAddTodo: () -> Unit = {},
    onDeleteTodo: () -> Unit = {}
) {
    val state = viewModel.myTodoEditState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadTodos()
    }

    when (val uiState = state.value) {
        is MyTodoEditState.Success -> {
            val currentTodos = uiState.todos.filter { !it.isPast }
            val pastTodos = uiState.todos.filter { it.isPast }
            
            MyTodoEditScreen(
                currentTodos = currentTodos,
                pastTodos = pastTodos,
                onBackClick = onBackClick,
                onTodoClick = onTodoClick,
                onAddTodo = onAddTodo,
                onDeleteTodo = onDeleteTodo
            )
        }
        is MyTodoEditState.Loading -> {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
        is MyTodoEditState.Error -> {
            // Error state - show empty screen with error message
            MyTodoEditScreen(
                currentTodos = emptyList(),
                pastTodos = emptyList(),
                onBackClick = onBackClick,
                onTodoClick = onTodoClick,
                onAddTodo = onAddTodo,
                onDeleteTodo = onDeleteTodo
            )
        }
    }
}