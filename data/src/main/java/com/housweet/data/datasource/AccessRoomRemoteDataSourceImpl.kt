package com.housweet.data.datasource

import com.housweet.data.BuildConfig
import com.housweet.data.constants.ApiEndpoints
import com.housweet.data.network.KtorService
import com.housweet.data.request.AccessRoomRequest
import com.housweet.data.request.CreateRoomRequest
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccessRoomRemoteDataSourceImpl @Inject constructor(
    private val ktorService: KtorService
) : AccessRoomRemoteDataSource {
    private val client: HttpClient
        get() = ktorService.getHttpClient()

    private val baseUrl = BuildConfig.BASE_URL

    override suspend fun createRoom(name: String): Boolean {
        val response = client.post("$baseUrl/${ApiEndpoints.Room.ROOMS}") {
            contentType(ContentType.Application.Json)
            setBody(CreateRoomRequest(name))
        }

        return response.status == HttpStatusCode.Created
    }

    override suspend fun accessRoomWithInviteCode(inviteCode: String): Boolean {
        val response = client.post("$baseUrl/${ApiEndpoints.Room.ROOMS_INVITE}") {
            contentType(ContentType.Application.Json)
            setBody(AccessRoomRequest(inviteCode))
        }

        return response.status == HttpStatusCode.OK
    }
}