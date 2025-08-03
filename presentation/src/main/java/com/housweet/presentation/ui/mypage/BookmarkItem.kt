package com.housweet.presentation.ui.mypage

data class BookmarkItem(
    val id: Int,
    val title: String,
    val price: String,
    val location: String,
    val ageGender: String,
    val bookmarked: Boolean
)
