package com.housweet.data.network

import com.housweet.data.network.dto.MyRoomResponse
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.headers
import javax.inject.Inject

// 데이터 소스 구현체, 실제 Ktor 통신 구현
class RoomRemoteDataSourceImpl @Inject constructor(
    private val ktorService: KtorService
): RoomRemoteDataSource {
    override suspend fun getMyRoomInfo(accessToken: String): MyRoomResponse {
        val client = ktorService.createHttpClient()
        return client.get("http://43.200.10.89/room/rooms/me/") {
            headers {
                append("Authorization", "Bearer $accessToken")
            }
            contentType(ContentType.Application.Json)
        }.body()
    }
}