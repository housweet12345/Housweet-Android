package com.housweet.data.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class CreateChatRoomMinimal(
    val room_id: Int,
    val created: Boolean
)
