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
)