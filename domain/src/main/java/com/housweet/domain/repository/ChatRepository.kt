package com.housweet.domain.repository

import com.housweet.domain.model.ChatMessage
import com.housweet.domain.model.ChatUser

interface ChatRepository {
    suspend fun getChatUsers(senderId:Int): List<ChatUser>
    suspend fun sendMessage(senderId: Int, receiverId: Int, message: String): Boolean
    suspend fun getChatMessages(senderId: Int, receiverId: Int): List<ChatMessage>
    suspend fun deleteRoom(roomId: Int): Result<Unit>
    suspend fun reportRoom(roomId: Int): Result<Unit>
    suspend fun createRoom(senderId: Int, receiverId: Int): Result<Int>
}