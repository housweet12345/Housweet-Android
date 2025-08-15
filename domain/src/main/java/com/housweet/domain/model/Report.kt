package com.housweet.domain.model

data class Report(
    val id: Int,
    val type: String,
    val metaData: ReportMetaData,
    val createdAt: String
)

data class ReportMetaData(
    val roomPostingId: Int
)
