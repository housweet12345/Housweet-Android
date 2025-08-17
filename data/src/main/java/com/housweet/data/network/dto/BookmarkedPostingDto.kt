package com.housweet.data.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BookmarkedPostingDto(
    val id: Int,
    val title: String,
    @SerialName("image_uri") val imageUri: String? = null,
    val rent: Int,
    val deposit: Int,
    @SerialName("age_range_and_gender") val ageRangeAndGender: String,
    @SerialName("area_text") val areaText: String,
    @SerialName("bookmarked_at") val bookmarkedAt: String
)

@Serializable
data class BookmarkedPostingListResponse(
    val data: List<BookmarkedPostingDto>
)

fun BookmarkedPostingDto.toDomain(): com.housweet.domain.model.BookmarkItem {
    return com.housweet.domain.model.BookmarkItem(
        id = id,
        title = title,
        imageUri = imageUri,
        rent = rent,
        deposit = deposit,
        ageRangeAndGender = ageRangeAndGender,
        areaText = areaText,
        bookmarkedAt = bookmarkedAt
    )
}
