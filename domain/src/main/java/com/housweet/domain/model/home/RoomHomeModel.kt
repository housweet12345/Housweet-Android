package com.housweet.domain.model.home

data class RoomHomeModel(
    val roomId: Int,
    val roomName: String,
    val daysTogether: Int,
    val members: List<RoomMemberModel>
)