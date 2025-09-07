package com.housweet.data.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReportRoomPostRequest(
    @SerialName("type")
    val type: String,
    @SerialName("id")
    val id: Int
)