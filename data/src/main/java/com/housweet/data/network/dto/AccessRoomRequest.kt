package com.housweet.data.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AccessRoomRequest(
    @SerialName("invite_code")
    val inviteCode: String
)