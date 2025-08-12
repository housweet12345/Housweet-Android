package com.housweet.data.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VisibilityRequestDto(
    @SerialName("is_visible") val isVisible: Boolean
)
