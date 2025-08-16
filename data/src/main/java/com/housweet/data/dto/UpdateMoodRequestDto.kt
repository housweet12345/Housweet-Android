package com.housweet.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateMoodRequestDto(
    @SerialName("feeling")
    val feeling: String
)