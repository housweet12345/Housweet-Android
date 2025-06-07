package com.housweet.data.network.dto

import com.housweet.domain.model.AuthToken
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponseDto(
    @SerialName("access")
    val accessToken: String,

    @SerialName("refresh")
    val refreshToken: String,
)

fun LoginResponseDto.toAuthToken(): AuthToken {
    return AuthToken(
        accessToken = accessToken,
        refreshToken = refreshToken
    )
}