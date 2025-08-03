package com.housweet.domain.model

data class RoomPostsByLocationDataModel(
    val id: Int,
    val title: String,
    val isBookmarked: Boolean,
    val imageUri: String,
    val rent: Int,
    val deposit: Int,
    val ageRangeAndGender: String,
)