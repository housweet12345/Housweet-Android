package com.housweet.data.datasource

import com.housweet.data.BuildConfig
import com.housweet.data.constants.ApiEndpoints
import com.housweet.data.network.KtorService
import com.housweet.data.response.NoticeResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoticeRemoteDataSourceImpl @Inject constructor(
    private val ktorService: KtorService
) : NoticeRemoteDataSource {
    private val client: HttpClient
        get() = ktorService.getHttpClient()
    private val base = BuildConfig.BASE_URL

    // 공통 헬퍼: 성공코드 확인 → JSON 파싱(실패시 raw 포함해 친절한 에러)
    private suspend inline fun <reified T> HttpResponse.requireJsonOrThrow(endpoint: String): T {
        if (!status.isSuccess()) {
            throw ResponseException(this, "$endpoint failed: $status ${runCatching { bodyAsText() }.getOrNull()}")
        }
        return runCatching { body<T>() }.getOrElse { ex ->
            val raw = runCatching { bodyAsText() }.getOrNull()
            throw ResponseException(this, "$endpoint parse error: ${ex.message}\nraw=$raw")
        }
    }

    override suspend fun getNotices(): List<NoticeResponse> {
        val res: HttpResponse = client.get("$base/${ApiEndpoints.Communications.NOTICES}")
        return res.requireJsonOrThrow("getNotices")
    }

    override suspend fun getNotice(id: Int): NoticeResponse {
        val res: HttpResponse = client.get("$base/${ApiEndpoints.Communications.noticeById(id)}")
        return res.requireJsonOrThrow("getNotice($id)")
    }
}