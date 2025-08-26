package com.housweet.data.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PostingDetailResponse(
    val title: String? = null,
    val content: String? = null,
    @SerialName("image_uri") val imageUri: String? = null,
//    @SerialName("images") val images: List<String>? = null,

    @SerialName("traffic_tags") val trafficTags: List<String>? = null,
    @SerialName("size_of_house_tags") val sizeOfHouseTags: List<String>? = null,
    @SerialName("infra_tags") val infraTags: List<String>? = null,
    @SerialName("life_pattern_tags") val lifePatternTags: List<String>? = null,
    @SerialName("tidying_up_habit_tags") val tidyingUpHabitTags: List<String>? = null,
    @SerialName("personality_tags") val personalityTags: List<String>? = null,

    val rent: Int? = null,
    val deposit: Int? = null,
    @SerialName("management_fee") val managementFee: Int? = null,
    @SerialName("available_from") val availableFrom: String? = null,

    @SerialName("lot_number_address") val lotNumberAddress: String? = null,
    @SerialName("road_address") val roadAddress: String? = null,
    @SerialName("detailed_address") val detailedAddress: String? = null,

//    val si: Int? = null,
//    val gu: Int? = null,
//    val dong: Long? = null,

    val si: String? = null,
    val gu: String? = null,
    val dong: String? = null,

    @SerialName("is_visible") val isVisible: Boolean? = null
)