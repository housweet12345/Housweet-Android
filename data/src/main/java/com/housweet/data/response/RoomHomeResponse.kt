package com.housweet.data.response

import com.housweet.domain.model.home.RoomHomeModel
import kotlinx.serialization.Serializable

@Serializable
data class RoomHomeResponse(
    val data: RoomHomeData
) {
    fun mapToRoomHomeModel(): RoomHomeModel {
        return data.mapToRoomHomeModel()
    }
}