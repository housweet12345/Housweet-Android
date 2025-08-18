package com.housweet.data.network

import android.util.Log
import com.housweet.data.network.dto.ChatMessageResponse
import com.housweet.data.network.dto.ChatUserDto
import com.housweet.data.network.dto.ChatUsersEnvelope
import com.housweet.data.network.dto.CreateChatRoomMinimal
import com.housweet.data.network.dto.SendMessageResponse
import com.housweet.data.network.dto.SimpleResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import javax.inject.Inject

class ChatRemoteDataSourceImpl @Inject constructor(
    private val client: HttpClient
) : ChatRemoteDataSource {
    private val base = "http://54.180.30.121:8000"

    override suspend fun getChatUsers(senderId: Int): List<ChatUserDto> {
        return try {
            val res = client.get("$base/chat/view-room/$senderId/")
            if (res.status.isSuccess()) {
                val envelope: ChatUsersEnvelope = res.body()
                envelope.results
            } else {
                val err = res.bodyAsText()
                Log.e("KtorClient", "getChatUsers failed ${res.status}: $err")
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("KtorClient", "getChatUsers exception", e)
            emptyList()
        }
    }

    override suspend fun sendMessage(senderId: Int, receiverId: Int, message: String): Boolean {
        return try {
            val res = client.post("$base/chat/send/$senderId/$receiverId/") {
                contentType(ContentType.Application.Json)
                setBody(mapOf("message" to message))
            }
            if (res.status.isSuccess()) {
                val body: SendMessageResponse = res.body()
                body.success
            } else {
                val err = res.bodyAsText()
                Log.e("KtorClient", "sendMessage failed ${res.status}: $err")
                false
            }
        } catch (e: Exception) {
            Log.e("KtorClient", "sendMessage exception", e)
            false
        }
    }

    override suspend fun getChatMessages(senderId: Int, receiverId: Int): List<ChatMessageResponse> {
        return try {
            val res = client.get("$base/chat/$senderId/$receiverId/messages/")
            if (res.status.isSuccess()) {
                res.body()
            } else {
                val err = res.bodyAsText()
                Log.e("KtorClient", "getChatMessages failed ${res.status}: $err")
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("KtorClient", "getChatMessages exception", e)
            emptyList()
        }
    }

    // 채팅방 삭제: POST /chat/room/{receiver_id}/delete/
    override suspend fun deleteRoom(roomId: Int): Result<Unit> {
        return try {
            val res = client.post("$base/chat/room/$roomId/delete/") {
                contentType(ContentType.Application.Json)
            }
            if (res.status.isSuccess()) {
                Result.success(Unit)
            } else {
                val err = res.bodyAsText()
                Result.failure(IllegalStateException("Delete failed (${res.status.value}) $err"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // 신고(차단): POST /chat/block/{room_id}/
    override suspend fun reportRoom(roomId: Int): Result<Unit> {
        return try {
            val res = client.post("$base/chat/block/$roomId/") {
                contentType(ContentType.Application.Json)
            }
            if (res.status.isSuccess()) {
                Result.success(Unit)
            } else {
                val err = res.bodyAsText()
                Result.failure(IllegalStateException("Report failed (${res.status.value}) $err"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun createRoom(senderId: Int, receiverId: Int): Result<Int> {
        return try {
            val res = client.post("$base/chat/create_room/$senderId/$receiverId/") {
                contentType(ContentType.Application.Json)
            }
            if (res.status.isSuccess()) {
                val body: CreateChatRoomMinimal = res.body()
                if (body.created) Result.success(body.room_id)
                else Result.failure(IllegalStateException("Room not created"))
            } else {
                val err = res.bodyAsText()
                Result.failure(IllegalStateException("Create room failed (${res.status.value}) $err"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}