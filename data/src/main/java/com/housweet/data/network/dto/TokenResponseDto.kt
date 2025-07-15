package com.housweet.data.network.dto

import com.housweet.domain.model.AuthToken
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TokenResponseDto(
    @SerialName("access")
    val accessToken: String,
)