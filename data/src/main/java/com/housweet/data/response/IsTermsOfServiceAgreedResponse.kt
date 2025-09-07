package com.housweet.data.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class IsTermsOfServiceAgreedResponse(
    @SerialName("id")
    val id: Int,
    @SerialName("user")
    val user: Int,
    @SerialName("terms_of_service_agreed")
    val termsOfServiceAgreed: Boolean
)