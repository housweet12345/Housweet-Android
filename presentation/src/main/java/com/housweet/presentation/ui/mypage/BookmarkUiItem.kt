package com.housweet.presentation.ui.mypage

import com.housweet.domain.model.BookmarkItem

data class BookmarkUiItem(
    val id: Int,
    val title: String,
    val thumbnailUrl: String?,
    val price: String,           // "보증금 294 / 월세 47"
    val ageGender: String,
    val bookmarked: Boolean = true
)

fun BookmarkItem.toUi(): BookmarkUiItem {
    fun Int.toManwon() = (this / 10_000).toString()
    return BookmarkUiItem(
        id = id,
        title = title,
        thumbnailUrl = imageUri,
        price = "보증금 ${deposit.toManwon()} / 월세 ${rent.toManwon()}",
        ageGender = ageRangeAndGender,
        bookmarked = true
    )
}