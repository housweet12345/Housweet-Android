package com.housweet.data.repository

import com.housweet.data.network.ReportRemoteDataSource
import com.housweet.data.network.dto.ReportRequest
import com.housweet.domain.model.Report
import com.housweet.domain.repository.ReportRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReportRepositoryImpl @Inject constructor(
    private val reportRemoteDataSource: ReportRemoteDataSource
) : ReportRepository {
    override suspend fun report(type: String, id: Int): Report {
        return reportRemoteDataSource.report(ReportRequest(type, id)).toDomain()
    }
}
