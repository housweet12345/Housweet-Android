package com.housweet.data.mapper

import com.housweet.data.network.dto.ReportResponse
import com.housweet.domain.model.Report
import com.housweet.domain.model.ReportMetaData

fun ReportResponse.toDomain(): Report {
    return Report(
        id = id,
        type = type,
        metaData = ReportMetaData(
            roomPostingId = metaData.roomPostingId
        ),
        createdAt = createdAt
    )
}
