package com.housweet.data.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class KakaoLoginRequest(
    @SerialName("social_id")
    val socialId: String,
    @SerialName("access_token")
    val accessToken: String,
    @SerialName("email")
    val email: String
)