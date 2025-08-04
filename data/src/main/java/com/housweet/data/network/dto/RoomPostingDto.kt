package com.housweet.data.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class RoomPostingDto(
    val id: Int,
    val title: String,
    val content: String = "",
    val is_visible: Boolean = true,
    val rent: Int,
    val deposit: Int,
    val image_uri: String? = null,
    val created_at: String = "",
    val age_range_and_gender: String = ""
)