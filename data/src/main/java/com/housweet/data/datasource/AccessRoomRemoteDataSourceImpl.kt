package com.housweet.data.datasource

import com.housweet.data.manager.BaseUrlManager
import com.housweet.data.network.KtorService
import com.housweet.data.request.AccessRoomRequest
import com.housweet.data.request.CreateRoomRequest
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccessRoomRemoteDataSourceImpl @Inject constructor(
    private val ktorService: KtorService,
    private val baseUrlManager: BaseUrlManager
) : AccessRoomRemoteDataSource {
    private val httpClient: HttpClient
        get() = ktorService.getHttpClient()

    override suspend fun createRoom(name: String): Boolean {
        val currentBaseUrl = baseUrlManager.getBaseUrl()
        val response = httpClient.post("$currentBaseUrl/room/rooms/") {
            contentType(ContentType.Application.Json)
            setBody(CreateRoomRequest(name))
        }

        return response.status.value == 201
    }

    override suspend fun accessRoomWithInviteCode(inviteCode: String): Boolean {
        val currentBaseUrl = baseUrlManager.getBaseUrl()
        val response = httpClient.post("$currentBaseUrl/room/rooms/invite/") {
            contentType(ContentType.Application.Json)
            setBody(AccessRoomRequest(inviteCode))
        }

        return response.status.value == 200
    }
}