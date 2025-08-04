package com.housweet.data.network.dto

import com.housweet.domain.model.RoomPost

fun RoomPostingDto.toRoomPost(): RoomPost {
    return RoomPost(
        id = id,
        title = title,
        deposit = deposit.toString(),
        rent = rent.toString(),
        ageRangeAndGender = age_range_and_gender,
        imageUrl = image_uri,
        priceInfo = "보증금 ${deposit / 10000} / 월세 ${rent / 10000}",
        metaInfo = age_range_and_gender,
        isHidden = !is_visible
    )
}