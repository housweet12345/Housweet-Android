package com.housweet.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BlockUserRequestDto(
    @SerialName("blocked_user_id")
    val blockedUserId: Int
)