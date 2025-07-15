package com.housweet.data.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateRoomRequest(
    @SerialName("name")
    val name: String
)