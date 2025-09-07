package com.housweet.data.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BlockUserResponse(
    @SerialName("detail")
    val detail: String
)