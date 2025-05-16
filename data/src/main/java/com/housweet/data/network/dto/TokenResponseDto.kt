package com.housweet.data.network.dto

import androidx.compose.runtime.Immutable
import com.housweet.domain.model.AuthToken
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Immutable
data class TokenResponseDto(
    @SerialName("access")
    val accessToken: String,

    @SerialName("refresh")
    val refreshToken: String
)

fun TokenResponseDto.toAuthToken(): AuthToken {
    return AuthToken(
        accessToken = accessToken,
        refreshToken = refreshToken,
        expiresIn = 0
    )
}