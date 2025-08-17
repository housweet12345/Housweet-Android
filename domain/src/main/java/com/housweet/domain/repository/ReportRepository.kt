package com.housweet.domain.repository

import com.housweet.domain.model.Report

interface ReportRepository {
    suspend fun report(type: String, id: Int): Report
}
