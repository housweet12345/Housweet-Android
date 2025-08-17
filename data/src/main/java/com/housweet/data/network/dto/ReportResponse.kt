package com.housweet.data.network.dto

import com.housweet.domain.model.Report
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReportResponse(
    @SerialName("id")
    val id: Int,
    @SerialName("type")
    val type: String,
    @SerialName("meta_data")
    val metaData: ReportMetaData,
    @SerialName("created_at")
    val createdAt: String
){
    fun toDomain(): Report {
        return Report(
            id = id,
            type = type,
            metaData = com.housweet.domain.model.ReportMetaData(
                roomPostingId = metaData.roomPostingId
            ),
            createdAt = createdAt
        )
    }
}

@Serializable
data class ReportMetaData(
    @SerialName("room_posting_id")
    val roomPostingId: Int
)
