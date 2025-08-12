package com.housweet.data.network

import com.housweet.data.BuildConfig
import com.housweet.data.network.dto.GetNearbyPostCountResponseListDto
import com.housweet.data.network.dto.GetRoomPostDetailResponseDto
import com.housweet.data.network.dto.GetRoomPostsByLocationResponseDto
import com.housweet.data.network.dto.GetRoomPostsByLocationResponseListDto
import com.housweet.data.network.dto.ReportRoomPostRequest
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import java.net.URLDecoder
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CommunityRemoteDataSourceImpl @Inject constructor(
    private val ktorClient: KtorService
): CommunityRemoteDataSource {
    companion object {
        private const val BASE_URL = BuildConfig.BASE_URL
    }

    private val httpClient by lazy { ktorClient.createHttpClient() }

    override suspend fun getNearbyPostCount(
        latitude: Double,
        longitude: Double,
        filteringDistance: Int
    ): GetNearbyPostCountResponseListDto {
        return httpClient.get("$BASE_URL/room/region/near/") {
            parameter("latitude", latitude)
            parameter("longitude", longitude)
            parameter("filtering_distance", filteringDistance)
        }.body()
    }

    override suspend fun getRoomPostsByLocation(searchWord: String): GetRoomPostsByLocationResponseListDto {
        return httpClient.get("$BASE_URL/room/room-postings/") {
            parameter("search_word", URLDecoder.decode(searchWord, "UTF-8"))
        }.body()
    }

    override suspend fun clickBookMark(roomPostingId: Int): Boolean {
        val response = httpClient.post("$BASE_URL/room/room-postings/${roomPostingId}/bookmark/")
        return response.status.value == 201
    }

    override suspend fun unClickBookMark(roomPostingId: Int): Boolean {
        val response = httpClient.delete("$BASE_URL/room/room-postings/${roomPostingId}/bookmark/")
        return response.status.value == 200
    }

    override suspend fun getRoomPostDetail(roomPostingId: Int): GetRoomPostDetailResponseDto {
        return httpClient.get("$BASE_URL/room/room-postings/${roomPostingId}/").body()
    }

    override suspend fun reportRoomPost(roomPostingId: Int): Boolean {
        val response = httpClient.post("$BASE_URL/report/") {
            setBody(ReportRoomPostRequest("room_posting", roomPostingId))
        }

        return response.status.value == 201
    }
}