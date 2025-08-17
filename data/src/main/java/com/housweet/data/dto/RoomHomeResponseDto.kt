package com.housweet.data.dto

import com.housweet.domain.model.home.RoomHomeModel
import kotlinx.serialization.Serializable

@Serializable
data class RoomHomeResponseDto(
    val data: RoomHomeDto
) {
    fun mapToRoomHomeModel(): RoomHomeModel {
        return data.mapToRoomHomeModel()
    }
}