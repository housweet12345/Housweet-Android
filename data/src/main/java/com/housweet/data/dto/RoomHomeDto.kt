package com.housweet.data.dto

import com.housweet.domain.model.home.RoomHomeModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RoomHomeDto(
    @SerialName("room_id")
    val roomId: Int,
    @SerialName("room_name")
    val roomName: String,
    @SerialName("days_together")
    val daysTogether: Int,
    val members: List<RoomMemberDto>
) {
    fun mapToRoomHomeModel(): RoomHomeModel {
        return RoomHomeModel(
            roomId = roomId,
            roomName = roomName,
            daysTogether = daysTogether,
            members = members.map { it.mapToRoomMemberModel() }
        )
    }
}