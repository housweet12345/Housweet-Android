package com.housweet.data.network

import com.housweet.data.network.dto.ChatMessageResponse
import com.housweet.data.network.dto.ChatUserDto
import com.housweet.data.network.dto.SendMessageResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import javax.inject.Inject

class ChatRemoteDataSourceImpl @Inject constructor(
    private val client: HttpClient
) : ChatRemoteDataSource {
    override suspend fun getChatUsers(): List<ChatUserDto> {
        return client.get("http://54.180.30.121:8000/chat/users/").body()
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
}