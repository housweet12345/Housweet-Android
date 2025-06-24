package com.housweet.presentation.ui.mypage

data class BookmarkItem(
    val id: Int,
    val title: String,
    val price: String,
    val location: String,
    val ageGender: String,
    val bookmarked: Boolean
)

val sampleBookmarks = listOf(
    BookmarkItem(1, "애완동물을 좋아하는 사람을 구하고 있어요.", "보증금 400 월세 20", "송파구 문정동", "20대 남자", true),
    BookmarkItem(2, "애완동물 좋아하는 사람을 구하고 있습니...", "보증금 400 월세 20", "송파구 문정동", "20대 남자", true)
)
