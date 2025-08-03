package com.housweet.data.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RoomPostingRequest(
    val room: Int,
    val si: Int,
    val gu: Int,
    val dong: Int,
    val title: String,
    val content: String,
    @SerialName("image_uri")
    val imageUri: String,
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
    val rent: Int,
    val deposit: Int,
    @SerialName("management_fee")
    val managementFee: Int,
    @SerialName("available_from")
    val availableFrom: String
)