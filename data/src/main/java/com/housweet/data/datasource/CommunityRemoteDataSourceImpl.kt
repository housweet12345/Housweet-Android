package com.housweet.data.datasource

import com.housweet.data.BuildConfig
import com.housweet.data.constants.ApiEndpoints
import com.housweet.data.network.KtorService
import com.housweet.data.request.ReportRoomPostRequest
import com.housweet.data.response.BookmarkedPostingListResponse
import com.housweet.data.response.GetNearbyPostCountResponseListDto
import com.housweet.data.response.GetRoomPostDetailResponse
import com.housweet.data.response.GetRoomPostsByLocationResponseList
import com.housweet.data.utils.requireJsonOrThrow
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
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CommunityRemoteDataSourceImpl @Inject constructor(
    private val ktorService: KtorService
): CommunityRemoteDataSource {
    companion object {
        private const val TAG = "CommunityRemote"
    }

    private val client: HttpClient
        get() = ktorService.getHttpClient()

    private val baseUrl = BuildConfig.BASE_URL

    override suspend fun getNearbyPostCount(
        latitude: Double,
        longitude: Double,
        filteringDistance: Int
    ): GetNearbyPostCountResponseListDto {
        val res: HttpResponse = client.get("$baseUrl/${ApiEndpoints.Room.REGION_NEAR}") {
            parameter("latitude", latitude)
            parameter("longitude", longitude)
            parameter("filtering_distance", filteringDistance)
        }
        return res.requireJsonOrThrow("getNearbyPostCount")
    }

    override suspend fun getRoomPostsByLocation(searchWord: String): GetRoomPostsByLocationResponseList {
        // Ktor가 query 인코딩을 처리하므로 그대로 전달
        val res: HttpResponse = client.get("$baseUrl/${ApiEndpoints.Room.ROOM_POSTINGS}") {
            parameter("search_word", searchWord)
        }
        return res.requireJsonOrThrow("getRoomPostsByLocation")
    }

    override suspend fun getBookmarkedPostings(): BookmarkedPostingListResponse {
        val res: HttpResponse = client.get("$baseUrl/${ApiEndpoints.Room.BOOKMARKED_POSTINGS}")
        return res.requireJsonOrThrow("getBookmarkedPostings")
    }

    override suspend fun clickBookMark(roomPostingId: Int): Boolean {
        val res: HttpResponse = client.post("$baseUrl/${ApiEndpoints.Room.bookmarkByPostingId(roomPostingId)}")
        // 서버에 따라 200/201/204 다양—전부 성공으로 인정
        return res.status in setOf(HttpStatusCode.OK, HttpStatusCode.Created, HttpStatusCode.NoContent)
    }

    override suspend fun unClickBookMark(roomPostingId: Int): Boolean {
        val res: HttpResponse = client.delete("$baseUrl/${ApiEndpoints.Room.bookmarkByPostingId(roomPostingId)}")
        return res.status in setOf(HttpStatusCode.OK, HttpStatusCode.NoContent)
    }

    override suspend fun getRoomPostDetail(roomPostingId: Int): GetRoomPostDetailResponse {
        val res: HttpResponse = client.get("$baseUrl/${ApiEndpoints.Room.postingById(roomPostingId)}")
        return res.requireJsonOrThrow("getRoomPostDetail")
    }

    override suspend fun reportRoomPost(roomPostingId: Int): Boolean {
        val res: HttpResponse = client.post("$baseUrl/${ApiEndpoints.Report.REPORT}") {
            contentType(ContentType.Application.Json)
            setBody(ReportRoomPostRequest(type = "room_posting", id = roomPostingId))
        }
        return res.status in setOf(HttpStatusCode.OK, HttpStatusCode.Created, HttpStatusCode.NoContent)
    }
}