package com.housweet.data.network.dto

import androidx.compose.runtime.Immutable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Immutable
data class RefreshTokenRequest(
    @SerialName("refresh_token")
    val refreshToken: String
)