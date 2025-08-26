package com.housweet.data.response

import com.housweet.domain.model.RoomPostsByLocationDataModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetRoomPostsByLocationResponseList(
    @SerialName("data")
    val data: List<GetRoomPostsByLocationResponse>
)

@Serializable
data class GetRoomPostsByLocationResponse(
    @SerialName("id")
    val id: Int,
    @SerialName("user_id")
    val userId: Int,
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

fun GetRoomPostsByLocationResponseList.toRoomPostsByLocationDataModel(): List<RoomPostsByLocationDataModel> {
    return this.data.map {
        RoomPostsByLocationDataModel(
            id = it.id,
            userId = it.userId,
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