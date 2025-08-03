package com.housweet.data.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MyRoomResponse(
    val id: Int,
    val name: String,
    @SerialName("room_leader_id")
    val roomLeaderId: Int,
    @SerialName("invite_code")
    val inviteCode: String,
    @SerialName("date_of_created")
    val dateOfCreated: String,
    @SerialName("date_of_joined")
    val dateOfJoined: String
)