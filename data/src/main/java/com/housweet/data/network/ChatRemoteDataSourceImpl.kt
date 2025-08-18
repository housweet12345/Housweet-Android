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
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import javax.inject.Inject

class ChatRemoteDataSourceImpl @Inject constructor(
    private val client: HttpClient
) : ChatRemoteDataSource {
    override suspend fun getChatUsers(senderId: Int): List<ChatUserDto> {
        return try {
            val envelope: ChatUsersEnvelope =
                client.get("http://54.180.30.121:8000/chat/view-room/$senderId/").body()
            envelope.results
        } catch (e: Exception) {
            // 접속 실패/타임아웃 등에서 안전하게 빈 리스트 반환
            Log.e("KtorClient", "getChatUsers failed", e)
            emptyList()
        }
    }

    override suspend fun sendMessage(senderId: Int, receiverId: Int, message: String): Boolean {
        return try {
            val response: SendMessageResponse = client.post("http://54.180.30.121:8000/chat/send/$senderId/$receiverId/") {
                contentType(ContentType.Application.Json)
                setBody(mapOf("message" to message))
            }.body()
            response.success
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override suspend fun getChatMessages(senderId: Int, receiverId: Int): List<ChatMessageResponse> {
        return client.get("http://54.180.30.121:8000/chat/$senderId/$receiverId/messages/").body()
    }

    // 채팅방 삭제: POST /chat/room/{receiver_id}/delete/
    override suspend fun deleteRoom(roomId: Int): Result<Unit> {
        return try {
            val httpResponse = client.post("http://54.180.30.121:8000/chat/room/$roomId/delete/") {
                contentType(ContentType.Application.Json)
                // Authorization 필요 시:
                // header("Authorization", "Bearer ${token}")
            }
            if (httpResponse.status.isSuccess()) {
                // 서버가 {"detail":"..."}만 주더라도 성공 코드를 우선 신뢰
                Result.success(Unit)
            } else {
                val body = runCatching { httpResponse.body<SimpleResponse>() }.getOrNull()
                Result.failure(IllegalStateException(body?.detail ?: "Delete failed (${httpResponse.status.value})"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // 신고(차단): POST /chat/block/{room_id}/
    override suspend fun reportRoom(roomId: Int): Result<Unit> {
        return try {
            val httpResponse = client.post("http://54.180.30.121:8000/chat/block/$roomId/") {
                contentType(ContentType.Application.Json)
                // header("Authorization", "Bearer ${token}")
            }
            if (httpResponse.status.isSuccess()) {
                Result.success(Unit)
            } else {
                val body = runCatching { httpResponse.body<SimpleResponse>() }.getOrNull()
                Result.failure(IllegalStateException(body?.detail ?: "Report failed (${httpResponse.status.value})"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun createRoom(senderId: Int, receiverId: Int): Result<Int> {
        return try {
            val res: CreateChatRoomMinimal = client.post("http://54.180.30.121:8000/chat/create_room/$senderId/$receiverId/") {
                contentType(ContentType.Application.Json)
//                header("Authorization", "Bearer ${token}") // 필요 시
            }.body()
            if (res.created) Result.success(res.room_id)
            else Result.failure(IllegalStateException("Room not created"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}