package com.housweet.data.repository

import com.housweet.data.network.ChatRemoteDataSource
import com.housweet.data.network.dto.toDomain
import com.housweet.domain.model.ChatMessage
import com.housweet.domain.model.ChatUser
import com.housweet.domain.repository.ChatRepository
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val remote: ChatRemoteDataSource
) : ChatRepository {
    override suspend fun getChatUsers(senderId: Int): List<ChatUser> {
        return remote.getChatUsers(senderId).map {
//            ChatUser(it.id, it.username, it.email)
            ChatUser(it.room_id, it.sender_id, it.receiver_id, it.created_at, it.updated_at, it.is_blocked, it.counterpart_id)
        }
    }
    override suspend fun sendMessage(senderId: Int, receiverId: Int, message: String): Boolean {
        return remote.sendMessage(senderId, receiverId, message)
    }

    override suspend fun getChatMessages(senderId: Int, receiverId: Int): List<ChatMessage> {
        return remote.getChatMessages(senderId, receiverId).map { it.toDomain() }
    }
}