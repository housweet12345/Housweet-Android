package com.housweet.data.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChatUserDto(
    val room_id : Int,
    val sender_id : Int,
    val receiver_id : Int,
    val created_at : String,
    val updated_at : String,
    val is_blocked : Boolean,
    val counterpart_id : Int,
    val sender_nickname : String,
    val receiver_nickname : String,
    val counterpart_nickname : String,
)

@Serializable
data class ChatUsersEnvelope(
    @SerialName("results") val results: List<ChatUserDto>
)
