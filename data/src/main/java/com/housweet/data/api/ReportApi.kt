package com.housweet.data.api

import com.housweet.data.BuildConfig
import com.housweet.data.network.dto.ReportRequest
import com.housweet.data.network.dto.ReportResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import javax.inject.Inject

class ReportApi @Inject constructor(private val httpClient: HttpClient) {
    suspend fun report(request: ReportRequest): ReportResponse {
        val response = httpClient.post("${BuildConfig.BASE_URL}/report") {
            setBody(request)
        }
        return response.body()
    }
}
