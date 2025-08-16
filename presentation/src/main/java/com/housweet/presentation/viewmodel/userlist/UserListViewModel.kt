package com.housweet.presentation.viewmodel.userlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.housweet.domain.usecase.home.GetRoomHomeUseCase
import com.housweet.domain.usecase.home.GetRoomMembersUseCase
import com.housweet.presentation.ui.userlist.state.UserItem
import com.housweet.presentation.ui.userlist.state.UserListState
import com.housweet.presentation.ui.userlist.state.toUserItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject constructor(
    private val getRoomHomeUseCase: GetRoomHomeUseCase,
    private val getRoomMembersUseCase: GetRoomMembersUseCase
) : ViewModel() {

    private val _userListState = MutableStateFlow<UserListState>(UserListState.Loading)
    val userListState: StateFlow<UserListState> = _userListState.asStateFlow()

    fun loadUsers() {
        viewModelScope.launch {
            _userListState.value = UserListState.Loading
            
            // 먼저 roomId를 가져오기 위해 room home 정보 조회
            val roomHomeResult = getRoomHomeUseCase()
            roomHomeResult.onSuccess { roomHome ->
                // roomId를 사용해서 멤버 목록 조회
                val membersResult = getRoomMembersUseCase(roomHome.roomId)
                membersResult.onSuccess { members ->
                    val userItems = members.map { it.toUserItem() }
                    _userListState.value = UserListState.Success(userItems)
                }.onFailure { error ->
                    _userListState.value = UserListState.Error(
                        error.message ?: "사용자 목록을 불러오는데 실패했습니다."
                    )
                }
            }.onFailure { error ->
                _userListState.value = UserListState.Error(
                    error.message ?: "방 정보를 불러오는데 실패했습니다."
                )
            }
        }
    }
}