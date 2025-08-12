package com.housweet.domain.model

data class ChatMessage(
    val senderId: Int,
    val receiverId: Int,
    val content: String,
    val createdAt: String
)
