package com.housweet.data.response

import com.housweet.domain.model.AuthToken
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    @SerialName("access")
    val accessToken: String,

    @SerialName("refresh")
    val refreshToken: String,

    @SerialName("is_terms_of_service_agreed")
    val isTermsOfServiceAgreed: Boolean
)

fun LoginResponse.toAuthToken(): AuthToken {
    return AuthToken(
        accessToken = accessToken,
        refreshToken = refreshToken
    )
}