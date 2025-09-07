package com.housweet.data.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateMoodRequest(
    @SerialName("feeling")
    val feeling: String
)