package com.housweet.data.response

import com.housweet.domain.model.ChatMessage
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChatMessageResponse(
    @SerialName("sender_id") val senderId: Int,
    @SerialName("receiver_id") val receiverId: Int,
    val content: String,
    @SerialName("created_at") val createdAt: String,
    @SerialName("sender_nickname") val senderNickname: String,
    @SerialName("receiver_nickname") val receiverNickname: String
)

fun ChatMessageResponse.toDomain(): ChatMessage {
    return ChatMessage(
        senderId = senderId,
        receiverId = receiverId,
        content = content,
//        createdAt = createdAt.toKstIsoLocalMillis(),
        createdAt = createdAt,
        senderNickname = senderNickname,
        receiverNickname = receiverNickname
    )
}
