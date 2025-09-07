package com.housweet.data.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VisibilityRequest(
    @SerialName("is_visible") val isVisible: Boolean
)