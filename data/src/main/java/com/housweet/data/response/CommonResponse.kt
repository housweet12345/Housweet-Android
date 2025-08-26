package com.housweet.data.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SimpleResponse(
    val detail: String? = null,
    @SerialName("success") val success: Boolean? = null
)