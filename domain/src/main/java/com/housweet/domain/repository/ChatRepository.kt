package com.housweet.domain.repository

import com.housweet.domain.model.ChatMessage
import com.housweet.domain.model.ChatUser

interface ChatRepository {
    suspend fun getChatUsers(): List<ChatUser>
    suspend fun sendMessage(senderId: Int, receiverId: Int, message: String): Boolean
    suspend fun getChatMessages(senderId: Int, receiverId: Int): List<ChatMessage>
}