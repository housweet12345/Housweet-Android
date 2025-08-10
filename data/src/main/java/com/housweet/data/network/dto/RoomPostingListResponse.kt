package com.housweet.data.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RoomPostingListResponse(
    @SerialName("data") val data: List<RoomPostingDto> = emptyList()
)