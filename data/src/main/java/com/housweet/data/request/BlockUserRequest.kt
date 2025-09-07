package com.housweet.data.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BlockUserRequest(
    @SerialName("blocked_user_id")
    val blockedUserId: Int
)