package com.housweet.domain.model.home

data class RoomMemberModel(
    val id: Int,
    val userId: Int,
    val nickname: String,
    val profileImageUrl: String?,
    val feeling: String
)