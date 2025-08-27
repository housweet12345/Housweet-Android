package com.housweet.data.network

import android.util.Log
import com.housweet.data.BuildConfig
import com.housweet.data.local.AuthLocalDataSource
import com.housweet.data.response.RoomPostingListResponse
import com.housweet.data.request.VisibilityRequest
import com.housweet.data.mapper.toRoomPost
import com.housweet.domain.model.RoomPost
import com.housweet.domain.repository.RoomPostingRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
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
    private val client: HttpClient
        get() = ktorService.getHttpClient()
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


    override suspend fun updatePostVisibility(postingId: Int, isVisible: Boolean):Boolean {
        val token = authLocalDataSource.getAuthToken()?.accessToken.orEmpty()
        return try {
            client.patch("$BASE_URL/room/room-postings/$postingId/") {
                contentType(ContentType.Application.Json)
                headers { append("Authorization", "Bearer $token") }
                setBody(VisibilityRequest(isVisible))
            }
            true
        } catch (t: Throwable) {
            Log.e("RoomRepo", "updatePostVisibility failed", t)
            false
        }
    }

    override suspend fun deletePost(postingId: Int):Boolean {
        val token = authLocalDataSource.getAuthToken()?.accessToken.orEmpty()
        return try {
            client.delete("$BASE_URL/room/room-postings/$postingId/") {
                headers { append("Authorization", "Bearer $token") }
            }
            true
        } catch (t: Throwable) {
            Log.e("RoomRepo", "deletePost failed", t)
            false
        }
    }
}
