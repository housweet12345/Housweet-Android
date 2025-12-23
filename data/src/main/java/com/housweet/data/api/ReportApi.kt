package com.housweet.data.api

import com.housweet.data.manager.BaseUrlManager
import com.housweet.data.network.KtorService
import com.housweet.data.request.ReportRequest
import com.housweet.data.response.ReportResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import javax.inject.Inject

class ReportApi @Inject constructor(
    private val ktorService: KtorService,
    private val baseUrlManager: BaseUrlManager
) {
    private val httpClient: HttpClient
        get() = ktorService.getHttpClient()
    suspend fun report(request: ReportRequest): ReportResponse {
        val currentBaseUrl = baseUrlManager.getBaseUrl()
        val response = httpClient.post("$currentBaseUrl/report") {
            setBody(request)
        }
        return response.body()
    }
}
