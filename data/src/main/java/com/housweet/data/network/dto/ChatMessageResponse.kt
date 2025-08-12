package com.housweet.data.network.dto

import com.housweet.domain.model.ChatMessage
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChatMessageResponse(
    @SerialName("sender_id") val senderId: Int,
    @SerialName("receiver_id") val receiverId: Int,
    val content: String,
    @SerialName("created_at") val createdAt: String
)

fun ChatMessageResponse.toDomain(): ChatMessage {
    return ChatMessage(
        senderId = senderId,
        receiverId = receiverId,
        content = content,
        createdAt = createdAt
    )
}
