package com.housweet.data.network.dto

import com.housweet.domain.model.RoomPostsByLocationDataModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetRoomPostsByLocationResponseListDto(
    @SerialName("data")
    val data: List<GetRoomPostsByLocationResponseDto>
)

@Serializable
data class GetRoomPostsByLocationResponseDto(
    @SerialName("id")
    val id: Int,
    @SerialName("title")
    val title: String,
    @SerialName("is_bookmarked")
    val isBookmarked: Boolean,
    @SerialName("image_uri")
    val imageUri: String?,
    @SerialName("rent")
    val rent: Int,
    @SerialName("deposit")
    val deposit: Int,
    @SerialName("age_range_and_gender")
    val ageRangeAndGender: String,
    @SerialName("is_visible")
    val isVisible: Boolean
)

fun GetRoomPostsByLocationResponseListDto.toRoomPostsByLocationDataModel(): List<RoomPostsByLocationDataModel> {
    return this.data.map {
        RoomPostsByLocationDataModel(
            id = it.id,
            title = it.title,
            isBookmarked = it.isBookmarked,
            imageUri = it.imageUri ?: "",
            rent = it.rent,
            deposit = it.deposit,
            ageRangeAndGender = it.ageRangeAndGender,
            isVisible = it.isVisible
        )
    }
}