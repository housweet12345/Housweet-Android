package com.housweet.data.datasource

import com.housweet.data.response.ChatUserResponse
import com.housweet.data.response.ChatMessageResponse

interface ChatRemoteDataSource {
    suspend fun getChatUsers(senderId: Int): List<ChatUserResponse>

    suspend fun sendMessage(senderId: Int, receiverId: Int, message: String): Boolean

    suspend fun getChatMessages(senderId: Int, receiverId: Int): List<ChatMessageResponse>

    suspend fun deleteRoom(roomId: Int): Result<Unit>

    suspend fun reportRoom(roomId: Int): Result<Unit>

    suspend fun createRoom(senderId: Int, receiverId: Int): Result<Int>
}