package com.housweet.data.network.dto

import androidx.compose.runtime.Immutable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Immutable
data class KakaoLoginRequest(
    @SerialName("social_id")
    val socialId: String,
    @SerialName("access_token")
    val accessToken: String,
    @SerialName("email")
    val email: String
)