package com.housweet.data.api

import com.housweet.domain.model.ChatMessage
import retrofit2.http.GET
import retrofit2.http.Path

interface ChatApi {
    @GET("chat/send/{sender_id}/{receiver_id}/")
    suspend fun getChatMessages(
        @Path("sender_id") senderId: Int,
        @Path("receiver_id") receiverId: Int
    ): List<ChatMessage>
}
