package com.housweet.data.datasource

import com.housweet.data.BuildConfig
import com.housweet.data.constants.ApiEndpoints
import com.housweet.data.network.KtorService
import com.housweet.data.response.MyRoomResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomRemoteDataSourceImpl @Inject constructor(
    private val ktorService: KtorService
): RoomRemoteDataSource {
    private val baseUrl = BuildConfig.MY_HOUSE_BASE_URL

    private val client: HttpClient
        get() = ktorService.getHttpClient()

    override suspend fun getMyRoomInfo(accessToken: String): MyRoomResponse {
        val response = client.get("$baseUrl/${ApiEndpoints.Room.ROOMS_ME_WITH_SLASH}") {
            headers {
                append("Authorization", "Bearer $accessToken")
            }
            contentType(ContentType.Application.Json)
        }

        if (response.status == HttpStatusCode.NotFound) {
            throw ResponseException(response, "Room not found")
        }

        return response.body()
    }
}