package com.housweet.presentation.viewmodel.schedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.housweet.presentation.ui.schedule.state.MyTodoEditState
import com.housweet.presentation.model.schedule.EditableTodoInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyTodoEditViewModel @Inject constructor(
) : ViewModel() {
    
    private val _myTodoEditState: MutableStateFlow<MyTodoEditState> = MutableStateFlow(MyTodoEditState.Loading)
    val myTodoEditState: StateFlow<MyTodoEditState> = _myTodoEditState.asStateFlow()

    fun loadTodos() {
        viewModelScope.launch {
            _myTodoEditState.value = MyTodoEditState.Loading
            
            try {
                // TODO: UseCase를 통해 실제 데이터를 가져오도록 수정 필요
                val todos = listOf(
                    EditableTodoInfo(
                        id = 1,
                        title = "설거지하기",
                        isCompleted = false,
                        isPast = false
                    ),
                    EditableTodoInfo(
                        id = 2,
                        title = "분리수거하기",
                        isCompleted = true,
                        isPast = false
                    ),
                    EditableTodoInfo(
                        id = 3,
                        title = "청소기 돌리기",
                        isCompleted = true,
                        isPast = true
                    ),
                    EditableTodoInfo(
                        id = 4,
                        title = "빨래하기",
                        isCompleted = true,
                        isPast = true
                    )
                )
                
                _myTodoEditState.value = MyTodoEditState.Success(todos)
            } catch (e: Exception) {
                _myTodoEditState.value = MyTodoEditState.Error(
                    e.message ?: "할일 목록 로딩에 실패했습니다"
                )
            }
        }
    }

    fun updateTodo(todoId: Int, newTitle: String) {
        val currentState = _myTodoEditState.value
        if (currentState is MyTodoEditState.Success) {
            viewModelScope.launch {
                try {
                    // TODO: UseCase를 통해 실제 업데이트 로직 구현
                    // updateTodoUseCase(todoId, newTitle)
                    
                    val updatedTodos = currentState.todos.map { todo ->
                        if (todo.id == todoId) {
                            todo.copy(title = newTitle)
                        } else {
                            todo
                        }
                    }
                    
                    _myTodoEditState.value = MyTodoEditState.Success(updatedTodos)
                } catch (e: Exception) {
                    _myTodoEditState.value = MyTodoEditState.Error(
                        e.message ?: "할일 업데이트에 실패했습니다"
                    )
                }
            }
        }
    }

    fun toggleTodoComplete(todoId: Int) {
        val currentState = _myTodoEditState.value
        if (currentState is MyTodoEditState.Success) {
            viewModelScope.launch {
                try {
                    // TODO: UseCase를 통해 실제 토글 로직 구현
                    // toggleTodoCompleteUseCase(todoId)
                    
                    val updatedTodos = currentState.todos.map { todo ->
                        if (todo.id == todoId) {
                            todo.copy(isCompleted = !todo.isCompleted)
                        } else {
                            todo
                        }
                    }
                    
                    _myTodoEditState.value = MyTodoEditState.Success(updatedTodos)
                } catch (e: Exception) {
                    _myTodoEditState.value = MyTodoEditState.Error(
                        e.message ?: "할일 상태 변경에 실패했습니다"
                    )
                }
            }
        }
    }

    fun deleteTodo(todoId: Int) {
        val currentState = _myTodoEditState.value
        if (currentState is MyTodoEditState.Success) {
            viewModelScope.launch {
                try {
                    // TODO: UseCase를 통해 실제 삭제 로직 구현
                    // deleteTodoUseCase(todoId)
                    
                    val updatedTodos = currentState.todos.filter { it.id != todoId }
                    _myTodoEditState.value = MyTodoEditState.Success(updatedTodos)
                } catch (e: Exception) {
                    _myTodoEditState.value = MyTodoEditState.Error(
                        e.message ?: "할일 삭제에 실패했습니다"
                    )
                }
            }
        }
    }

    fun refreshTodos() {
        loadTodos()
    }
}