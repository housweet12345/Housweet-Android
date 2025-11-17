package com.housweet.data.datasource

import com.housweet.data.BuildConfig
import com.housweet.data.constants.ApiEndpoints
import com.housweet.data.network.KtorService
import com.housweet.data.request.RegisterHouseRequest
import com.housweet.data.request.UpdateHouseRequest
import com.housweet.data.response.PostingDetailResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import javax.inject.Inject

class HouseRegisterRemoteDataSourceImpl @Inject constructor(
    private val ktorService: KtorService
) : HouseRegisterRemoteDataSource {
    private val client: HttpClient
        get() = ktorService.getHttpClient()

    private val BASE_URL = BuildConfig.BASE_URL

    // 방 등록 (서버가 보디를 안 줘도 status만 체크하면 됨)
    override suspend fun registerHouse(body: RegisterHouseRequest) {
        val res: HttpResponse = client.post("$BASE_URL/${ApiEndpoints.Room.ROOM_POSTINGS}") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        if (!res.status.isSuccess()) {
            val text = runCatching { res.bodyAsText() }.getOrDefault("")
            throw ResponseException(res, "registerHouse failed: ${res.status} $text")
        }
        // 201/200 이면 정상 종료
    }

    // 방 공고 상세 조회
    override suspend fun getPostingDetail(id: Int): PostingDetailResponse {
        val res: HttpResponse = client.get("$BASE_URL/${ApiEndpoints.Room.postingById(id)}")
        return when (res.status) {
            HttpStatusCode.OK -> res.body()
            HttpStatusCode.NotFound -> {
                val text = runCatching { res.bodyAsText() }.getOrDefault("")
                throw ResponseException(res, "posting not found: $id $text")
                // 필요하면 null 리턴으로 바꿔서 상위에서 Empty 처리해도 됨
            }
            else -> {
                val text = runCatching { res.bodyAsText() }.getOrDefault("")
                throw ResponseException(res, "getPostingDetail failed: ${res.status} $text")
            }
        }
    }

    // 올린 방 수정
    override suspend fun updateHouse(id: Int, body: UpdateHouseRequest) {
        val res: HttpResponse = client.patch("$BASE_URL/${ApiEndpoints.Room.postingById(id)}") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        if (!res.status.isSuccess()) {
            val text = runCatching { res.bodyAsText() }.getOrDefault("")
            throw ResponseException(res, "updateHouse failed: ${res.status} $text")
        }
        // 200/204 등 성공이면 그냥 리턴
    }
}