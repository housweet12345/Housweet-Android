package com.housweet.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BlockUserResponseDto(
    @SerialName("detail")
    val detail: String
)