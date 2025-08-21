package com.housweet.data.network.dto

import com.housweet.domain.model.RoomPostDetailDataModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetRoomPostDetailResponseDto(
    @SerialName("id")
    val id: Int,
    @SerialName("user_id")
    val userId: Int,
    @SerialName("title")
    val title: String,
    @SerialName("content")
    val content: String,
    @SerialName("image_uri")
    val imageUri: String?,
    @SerialName("rent")
    val rent: Int,
    @SerialName("deposit")
    val deposit: Int,
    @SerialName("management_fee")
    val managementFee: Int,
    @SerialName("available_from")
    val availableFrom: String,
    @SerialName("age_range_and_gender")
    val ageRangeAndGender: String,
    @SerialName("is_bookmarked")
    val isBookmarked: Boolean,
    @SerialName("lot_number_address")
    val lotNumberAddress: String,
    @SerialName("road_address")
    val roadAddress: String,
    @SerialName("detailed_address")
    val detailedAddress: String,
    @SerialName("traffic_tags")
    val trafficTags: List<String>,
    @SerialName("size_of_house_tags")
    val sizeOfHouseTags: List<String>,
    @SerialName("infra_tags")
    val infraTags: List<String>,
    @SerialName("life_pattern_tags")
    val lifePatternTags: List<String>,
    @SerialName("tidying_up_habit_tags")
    val tidyingUpHabitTags: List<String>,
    @SerialName("personality_tags")
    val personalityTags: List<String>,
    @SerialName("nickname")
    val nickname: String,
    @SerialName("profile_image_url")
    val profileImageUrl: String?,
    @SerialName("created_at_kst")
    val createdAtKst: String
)

fun GetRoomPostDetailResponseDto.toRoomPostDetailDataModel(): RoomPostDetailDataModel {
    return RoomPostDetailDataModel(
        id = id,
        userId = userId,
        title = title,
        content = content,
        imageUri = imageUri ?: "",
        rent = rent,
        deposit = deposit,
        managementFee = managementFee,
        availableFrom = availableFrom,
        ageRangeAndGender = ageRangeAndGender,
        isBookmarked = isBookmarked,
        lotNumberAddress = lotNumberAddress,
        roadAddress = roadAddress,
        detailedAddress = detailedAddress,
        trafficTags = trafficTags,
        sizeOfHouseTags = sizeOfHouseTags,
        infraTags = infraTags,
        lifePatternTags = lifePatternTags,
        tidyingUpHabitTags = tidyingUpHabitTags,
        personalityTags = personalityTags,
        nickName = nickname,
        profileImageUrl = profileImageUrl ?: "",
        createdAtKst = createdAtKst
    )
}