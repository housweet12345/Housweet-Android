package com.housweet.data.network

import com.housweet.data.api.ReportApi
import com.housweet.data.request.ReportRequest
import com.housweet.data.network.dto.ReportResponse
import javax.inject.Inject
import javax.inject.Singleton

interface ReportRemoteDataSource {
    suspend fun report(request: ReportRequest): ReportResponse
}

@Singleton
class ReportRemoteDataSourceImpl @Inject constructor(
    private val reportApi: ReportApi
) : ReportRemoteDataSource {
    override suspend fun report(request: ReportRequest): ReportResponse {
        return reportApi.report(request)
    }
}
