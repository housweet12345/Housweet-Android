package com.housweet.data.network

import com.housweet.data.BuildConfig
import com.housweet.data.local.AuthLocalDataSource
import com.housweet.data.network.dto.RoomPostingListResponse
import com.housweet.data.network.dto.VisibilityRequestDto
import com.housweet.data.network.dto.toRoomPost
import com.housweet.domain.model.RoomPost
import com.housweet.domain.repository.RoomPostingRepository
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.headers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomPostingRepositoryImpl @Inject constructor(
    private val ktorService: KtorService,
    private val authLocalDataSource: AuthLocalDataSource
): RoomPostingRepository {

    private val client by lazy { ktorService.createHttpClient() }
    private val BASE_URL = BuildConfig.BASE_URL

    override suspend fun getMyRoomPostings(): List<RoomPost> {
        val token = authLocalDataSource.getAuthToken()?.accessToken.orEmpty()

        val response: RoomPostingListResponse = client.get("$BASE_URL/room/room-postings/me/") {
            headers {
                append("Authorization", "Bearer $token")
            }
        }.body()

        return response.data.map { it.toRoomPost() }
    }


    override suspend fun updatePostVisibility(postingId: Int, isVisible: Boolean) {
        val token = authLocalDataSource.getAuthToken()?.accessToken.orEmpty()
        client.patch("$BASE_URL/room/room-postings/$postingId/") {
            contentType(ContentType.Application.Json)
            headers {
                append("Authorization", "Bearer $token")
            }
            setBody(VisibilityRequestDto(isVisible))
        }
    }
}
