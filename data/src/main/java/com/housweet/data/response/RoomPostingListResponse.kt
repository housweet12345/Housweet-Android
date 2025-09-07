package com.housweet.data.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RoomPostingListResponse(
    @SerialName("data") val data: List<RoomPostingResponse> = emptyList()
)