package com.housweet.domain.model

data class BookmarkItem(
    val id: Int,
    val title: String,
    val imageUri: String?,          // "image_uri"
    val rent: Int,                  // 원 단위
    val deposit: Int,               // 원 단위
    val ageRangeAndGender: String,  // "age_range_and_gender"
    val bookmarkedAt: String,        // ISO 시간 문자열
)
