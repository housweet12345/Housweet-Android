package com.housweet.data.datasource

import android.util.Log
import com.housweet.data.network.KtorService
import com.housweet.data.response.MyRoomResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.headers
import javax.inject.Inject

// 데이터 소스 구현체, 실제 Ktor 통신 구현
class RoomRemoteDataSourceImpl @Inject constructor(
    private val ktorService: KtorService
): RoomRemoteDataSource {
    private val client: HttpClient
        get() = ktorService.getHttpClient()

    override suspend fun getMyRoomInfo(accessToken: String): MyRoomResponse {
        val response = client.get("http://43.200.10.89/room/rooms/me/") {
            headers {
                append("Authorization", "Bearer $accessToken")
            }
            contentType(ContentType.Application.Json)
        }

        if (response.bodyAsText().contains("Room not found")) throw Exception("Room not found")

        return response.body()
    }
}