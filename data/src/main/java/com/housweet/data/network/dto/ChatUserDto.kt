package com.housweet.data.network.dto

import com.housweet.data.network.FlexibleBooleanSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChatUserDto(
    val room_id : Int,
    val sender_id : Int,
    val receiver_id : Int,
    val created_at : String,
    val updated_at : String,
    @SerialName("is_blocked")
    @Serializable(with = FlexibleBooleanSerializer::class)
    val isBlocked: Boolean,
    val counterpart_id : Int,
    val sender_nickname : String,
    val receiver_nickname : String,
    val counterpart_nickname : String,
    val last_message_content : String,
    val last_message_created_at : String,
)

@Serializable
data class ChatUsersEnvelope(
    @SerialName("results") val results: List<ChatUserDto>
)
