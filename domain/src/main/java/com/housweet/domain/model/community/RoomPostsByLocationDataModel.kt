package com.housweet.domain.model.community

data class RoomPostsByLocationDataModel(
    val id: Int,
    val userId: Int,
    val title: String,
    val isBookmarked: Boolean,
    val imageUri: String,
    val rent: Int,
    val deposit: Int,
    val ageRangeAndGender: String,
    val isVisible: Boolean
)