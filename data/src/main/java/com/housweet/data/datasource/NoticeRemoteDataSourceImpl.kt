package com.housweet.data.datasource

import com.housweet.data.BuildConfig
import com.housweet.data.constants.ApiEndpoints
import com.housweet.data.network.KtorService
import com.housweet.data.response.NoticeResponse
import com.housweet.data.utils.requireJsonOrThrow
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
    private val baseUrl = BuildConfig.BASE_URL

    override suspend fun getNotices(): List<NoticeResponse> {
        val res: HttpResponse = client.get("$baseUrl/${ApiEndpoints.Communications.NOTICES}")
        return res.requireJsonOrThrow("getNotices")
    }

    override suspend fun getNotice(id: Int): NoticeResponse {
        val res: HttpResponse = client.get("$baseUrl/${ApiEndpoints.Communications.noticeById(id)}")
        return res.requireJsonOrThrow("getNotice($id)")
    }
}