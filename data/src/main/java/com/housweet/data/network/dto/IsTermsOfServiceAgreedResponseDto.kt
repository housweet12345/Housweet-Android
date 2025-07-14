package com.housweet.data.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class IsTermsOfServiceAgreedResponseDto(
    @SerialName("id")
    val id: Int,
    @SerialName("user")
    val user: Int,
    @SerialName("terms_of_service_agreed")
    val termsOfServiceAgreed: Boolean
)