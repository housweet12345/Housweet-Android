package com.housweet.data.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateRoomRequest(
    @SerialName("name")
    val name: String
)