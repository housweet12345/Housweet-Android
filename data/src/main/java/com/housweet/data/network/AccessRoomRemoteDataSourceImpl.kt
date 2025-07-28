package com.housweet.data.network

import com.housweet.data.BuildConfig
import com.housweet.data.network.dto.AccessRoomRequest
import com.housweet.data.network.dto.CreateRoomRequest
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccessRoomRemoteDataSourceImpl @Inject constructor(
    private val ktorClient: KtorService
) : AccessRoomRemoteDataSource {
    companion object {
        private const val BASE_URL = BuildConfig.BASE_URL
    }

    private val httpClient by lazy { ktorClient.createHttpClient() }

    override suspend fun createRoom(name: String): Boolean {
        val response = httpClient.post("$BASE_URL/room/rooms/") {
            contentType(ContentType.Application.Json)
            setBody(CreateRoomRequest(name))
        }

        return response.status.value == 201
    }

    override suspend fun accessRoomWithInviteCode(inviteCode: String): Boolean {
        val response = httpClient.post("$BASE_URL/room/rooms/invite/") {
            contentType(ContentType.Application.Json)
            setBody(AccessRoomRequest(inviteCode))
        }

        return response.status.value == 200
    }
}