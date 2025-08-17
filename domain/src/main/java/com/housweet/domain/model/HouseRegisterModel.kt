package com.housweet.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class HouseRegisterModel(
    val room: Int,
    val si: Int,
    val gu: Int,
    val dong: Long,
    val title: String,
    val content: String,
    val imageUri: String,
//    val images: List<String>,
    val trafficTags: List<String>,
    val sizeOfHouseTags: List<String>,
    val infraTags: List<String>,
    val lifePatternTags: List<String>,
    val tidyingUpHabitTags: List<String>,
    val personalityTags: List<String>,
    val rent: Int,
    val deposit: Int,
    val managementFee: Int,
    val availableFrom: String
)