package com.housweet.data.network.dto

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
)

@Serializable
data class ReportMetaData(
    @SerialName("room_posting_id")
    val roomPostingId: Int
)
