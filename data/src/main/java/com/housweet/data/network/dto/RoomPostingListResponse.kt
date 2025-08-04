package com.housweet.data.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class RoomPostingListResponse(
    val data: List<RoomPostingDto>
)