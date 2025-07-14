package com.housweet.data.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AgreeTermsOfServiceRequest(
    @SerialName("terms_of_service_agreed")
    val termsOfServiceAgreed: Boolean
)