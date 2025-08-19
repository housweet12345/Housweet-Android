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
        return remote.getChatUsers(senderId).map { it ->
            ChatUser(
                room_id = it.room_id,
                sender_id = it.sender_id,
                receiver_id = it.receiver_id,
//                created_at = it.created_at.toKstIsoLocalMillis(),
                created_at = it.created_at.orEmpty(),
//                updated_at = it.updated_at.toKstIsoLocalMillis(),
                updated_at = it.updated_at.orEmpty(),
                is_blocked = it.isBlocked,
                counterpart_id = it.counterpart_id,
                sender_nickname = it.sender_nickname.orEmpty(),
                receiver_nickname = it.receiver_nickname.orEmpty(),
                counterpart_nickname = it.counterpart_nickname.orEmpty(),
                last_message_content = it.last_message_content.orEmpty(),
//                last_message_created_at = it.last_message_created_at.toKstIsoLocalMillis()
                last_message_created_at = it.last_message_created_at.orEmpty()
            )
        }
    }
    override suspend fun sendMessage(senderId: Int, receiverId: Int, message: String): Boolean {
        return remote.sendMessage(senderId, receiverId, message)
    }

    override suspend fun getChatMessages(senderId: Int, receiverId: Int): List<ChatMessage> {
        return remote.getChatMessages(senderId, receiverId).map { dto ->
            dto.toDomain()
        }
    }
    override suspend fun deleteRoom(roomId: Int): Result<Unit> = remote.deleteRoom(roomId)

    override suspend fun reportRoom(roomId: Int): Result<Unit> = remote.reportRoom(roomId)

    override suspend fun createRoom(senderId: Int, receiverId: Int): Result<Int> = remote.createRoom(senderId, receiverId)
}