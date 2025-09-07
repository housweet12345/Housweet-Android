package com.housweet.data.response

import kotlinx.serialization.Serializable

@Serializable
data class CreateChatRoomResponse(
    val room_id: Int,
    val created: Boolean
)