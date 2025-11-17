package com.housweet.data.api

import com.housweet.data.constants.ApiEndpoints
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

// Note: Retrofit 어노테이션은 컴파일 타임 상수만 허용하므로,
// ApiEndpoints.Chat.send() 함수는 사용할 수 없습니다.
// 대신 ApiEndpoints에 템플릿 형태의 상수를 추가하거나,
// 현재처럼 문자열 리터럴을 유지할 수 있습니다.
