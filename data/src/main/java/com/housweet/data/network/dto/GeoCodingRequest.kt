package com.housweet.data.network.dto

import androidx.compose.runtime.Immutable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Immutable
data class GeoCodingRequest (
    @SerialName("query")
    val query: String
)