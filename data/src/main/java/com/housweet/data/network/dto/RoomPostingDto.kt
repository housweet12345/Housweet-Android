package com.housweet.data.network.dto

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RoomPostingDto(
    val id: Int,

    @SerialName("user_id")
    val userId: Int = 0,

    val title: String = "",
    val content: String = "",

    @SerialName("image_uri")
    val imageUri: String? = "",

//    @SerialName("images")
//    val images: List<String> = emptyList(),

    val rent: Int = 0,
    val deposit: Int = 0,

    @SerialName("age_range_and_gender")
    val ageRangeAndGender: String = "",

    @SerialName("is_bookmarked")
    val isBookmarked: Boolean = false,

    @SerialName("traffic_tags")
    val trafficTags: List<String> = emptyList(),

    @SerialName("size_of_house_tags")
    val sizeOfHouseTags: List<String> = emptyList(),

    @SerialName("infra_tags")
    val infraTags: List<String> = emptyList(),

    @SerialName("life_pattern_tags")
    val lifePatternTags: List<String> = emptyList(),

    @SerialName("tidying_up_habit_tags")
    val tidyingUpHabitTags: List<String> = emptyList(),

    @SerialName("personality_tags")
    val personalityTags: List<String> = emptyList(),

    @SerialName("management_fee")
    val managementFee: Int = 0,

    @SerialName("available_from")
    val availableFrom: String = "",

    @SerialName("lot_number_address")
    val lotNumberAddress: String? = null,

    @SerialName("road_address")
    val roadAddress: String? = null,

    @SerialName("detailed_address")
    val detailedAddress: String? = null,

    @SerialName("is_visible")
    val isVisible: Boolean = true,

    @SerialName("area_text")
    val areaText: String? = null
)