package com.housweet.data.response

import com.housweet.data.network.FlexibleBooleanSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChatUserResponse(
    val room_id : Int,
    val sender_id : Int,
    val receiver_id : Int,
    val created_at : String? = null,
    val updated_at : String? = null,
    @SerialName("is_blocked")
    @Serializable(with = FlexibleBooleanSerializer::class)
    val isBlocked: Boolean = false,
    val counterpart_id : Int,
    val sender_nickname : String? = null,
    val receiver_nickname : String? = null,
    val counterpart_nickname : String? = null,
    val last_message_content : String? = null,
    val last_message_created_at : String? = null,
)

@Serializable
data class ChatUsersEnvelope(
    @SerialName("results") val results: List<ChatUserResponse>
)