package com.housweet.presentation.ui.userlist.state


sealed class UserListState {
    data object Loading : UserListState()
    data class Success(val userItems: List<UserItem>) : UserListState()
    data class Error(val message: String) : UserListState()
}