package com.housweet.data.model.request

import kotlinx.serialization.Serializable

@Serializable
data class RegisterHouseRequest(
    val room: Int,
//    val si: Int,
//    val gu: Int,
//    val dong: Long,
    val si: String,
    val gu: String,
    val dong: String,
    val title: String,
    val content: String,
    val image_uri: String,
//    val images: List<String>,
    val traffic_tags: List<String>,
    val size_of_house_tags: List<String>,
    val infra_tags: List<String>,
    val life_pattern_tags: List<String>,
    val tidying_up_habit_tags: List<String>,
    val personality_tags: List<String>,
    val rent: Int,
    val deposit: Int,
    val management_fee: Int,
    val available_from: String
)