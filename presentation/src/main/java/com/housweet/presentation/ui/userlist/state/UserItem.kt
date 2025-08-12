package com.housweet.presentation.ui.userlist.state

data class UserItem(
    val id: String,
    val name: String,
    val profileImageUrl: String? = null,
    val isHost: Boolean = false // 방장 여부
)