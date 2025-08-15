package com.housweet.data.network

import com.housweet.data.network.dto.ChatMessageResponse
import com.housweet.data.network.dto.ChatUserDto
import com.housweet.domain.model.ChatMessage

interface ChatRemoteDataSource {
    suspend fun getChatUsers(senderId: Int): List<ChatUserDto>

    suspend fun sendMessage(senderId: Int, receiverId: Int, message: String): Boolean

    suspend fun getChatMessages(senderId: Int, receiverId: Int): List<ChatMessageResponse>
}