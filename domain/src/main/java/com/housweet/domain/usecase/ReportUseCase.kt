package com.housweet.domain.usecase

import com.housweet.domain.model.Report
import com.housweet.domain.repository.ReportRepository
import javax.inject.Inject

class ReportUseCase @Inject constructor(
    private val reportRepository: ReportRepository
) {
    suspend operator fun invoke(type: String, id: Int): Report {
        return reportRepository.report(type, id)
    }
}
