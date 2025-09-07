package com.housweet.data.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AccessRoomRequest(
    @SerialName("invite_code")
    val inviteCode: String
)