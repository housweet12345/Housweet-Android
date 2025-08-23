package com.housweet.data.network

import com.housweet.data.BuildConfig
import com.housweet.data.network.dto.BookmarkedPostingListResponse
import com.housweet.data.network.dto.GetNearbyPostCountResponseListDto
import com.housweet.data.network.dto.GetRoomPostDetailResponseDto
import com.housweet.data.network.dto.GetRoomPostsByLocationResponseListDto
import com.housweet.data.request.ReportRoomPostRequest
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import java.net.URLDecoder
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CommunityRemoteDataSourceImpl @Inject constructor(
    private val ktorService: KtorService
): CommunityRemoteDataSource {
    companion object {
        private const val BASE_URL = BuildConfig.BASE_URL
        private const val TAG = "CommunityRemote"
    }

    private val httpClient: HttpClient
        get() = ktorService.getHttpClient()

    // 공통: 성공 코드 확인 후 JSON 파싱. 파싱 실패도 보기 좋게 에러 메시지 포함.
    private suspend inline fun <reified T> HttpResponse.requireJsonOrThrow(endpoint: String): T {
        if (!status.isSuccess()) {
            throw ResponseException(this, "$endpoint failed: $status ${runCatching { bodyAsText() }.getOrNull()}")
        }
        return runCatching { body<T>() }.getOrElse { ex ->
            val raw = runCatching { bodyAsText() }.getOrNull()
            throw ResponseException(this, "$endpoint parse error: ${ex.message}\nraw=$raw")
        }
    }

    override suspend fun getNearbyPostCount(
        latitude: Double,
        longitude: Double,
        filteringDistance: Int
    ): GetNearbyPostCountResponseListDto {
        val res: HttpResponse = httpClient.get("$BASE_URL/room/region/near/") {
            parameter("latitude", latitude)
            parameter("longitude", longitude)
            parameter("filtering_distance", filteringDistance)
        }
        return res.requireJsonOrThrow("getNearbyPostCount")
    }

    override suspend fun getRoomPostsByLocation(searchWord: String): GetRoomPostsByLocationResponseListDto {
        // Ktor가 query 인코딩을 처리하므로 그대로 전달
        val res: HttpResponse = httpClient.get("$BASE_URL/room/room-postings/") {
            parameter("search_word", searchWord)
        }
        return res.requireJsonOrThrow("getRoomPostsByLocation")
    }

    override suspend fun getBookmarkedPostings(): BookmarkedPostingListResponse {
        val res: HttpResponse = httpClient.get("$BASE_URL/room/room-postings/bookmarked-postings/")
        return res.requireJsonOrThrow("getBookmarkedPostings")
    }

    override suspend fun clickBookMark(roomPostingId: Int): Boolean {
        val res: HttpResponse = httpClient.post("$BASE_URL/room/room-postings/$roomPostingId/bookmark/")
        // 서버에 따라 200/201/204 다양—전부 성공으로 인정
        return res.status in setOf(HttpStatusCode.OK, HttpStatusCode.Created, HttpStatusCode.NoContent)
    }

    override suspend fun unClickBookMark(roomPostingId: Int): Boolean {
        val res: HttpResponse = httpClient.delete("$BASE_URL/room/room-postings/$roomPostingId/bookmark/")
        return res.status in setOf(HttpStatusCode.OK, HttpStatusCode.NoContent)
    }

    override suspend fun getRoomPostDetail(roomPostingId: Int): GetRoomPostDetailResponseDto {
        val res: HttpResponse = httpClient.get("$BASE_URL/room/room-postings/$roomPostingId/")
        return res.requireJsonOrThrow("getRoomPostDetail")
    }

    override suspend fun reportRoomPost(roomPostingId: Int): Boolean {
        val res: HttpResponse = httpClient.post("$BASE_URL/report/") {
            contentType(ContentType.Application.Json)
            setBody(ReportRoomPostRequest(type = "room_posting", id = roomPostingId))
        }
        return res.status in setOf(HttpStatusCode.OK, HttpStatusCode.Created, HttpStatusCode.NoContent)
    }
}