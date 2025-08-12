package com.housweet.presentation.viewmodel.userlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.housweet.presentation.ui.userlist.state.UserItem
import com.housweet.presentation.ui.userlist.state.UserListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject constructor(
    // repository 등 의존성 주입
) : ViewModel() {

    private val _userListState = MutableStateFlow<UserListState>(UserListState.Loading)
    val userListState: StateFlow<UserListState> = _userListState.asStateFlow()

    fun loadUsers() {
        viewModelScope.launch {
            try {
                _userListState.value = UserListState.Loading
                // API 호출 또는 repository에서 사용자 목록 가져오기
                // val users = userRepository.getUsers()
                // _userListState.value = UserListState.Success(users)

                val mockUsers = listOf(
                    UserItem("1", "홍길동", isHost = false),
                    UserItem("2", "홍길동", isHost = true), // 방장
                )
                _userListState.value = UserListState.Success(mockUsers)
            } catch (e: Exception) {
                _userListState.value = UserListState.Error(e.message ?: "사용자 목록을 불러오는데 실패했습니다.")
            }
        }
    }
}