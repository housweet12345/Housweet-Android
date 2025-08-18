package com.housweet.data.network.dto

import com.housweet.domain.model.MyHouse
import kotlinx.serialization.Serializable

@Serializable
data class MyHouseDto(
    val id: Int,
    val name: String,
    val room_leader_id: Int,
    val invite_code: String,
    val date_of_created: String,
    val date_of_joined: String
) {
    fun toDomain() = MyHouse(
        id = id,
        name = name,
        roomLeaderId = room_leader_id,
        inviteCode = invite_code,
        dateOfCreated = date_of_created,
        dateOfJoined = date_of_joined
    )
}

@Serializable
data class UpdateMyHouseNameRequest(
    val name: String
)
