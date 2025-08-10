package com.housweet.data.network.dto

import com.housweet.domain.model.RoomPost

fun RoomPostingDto.toRoomPost(): RoomPost {
    return RoomPost(
        id = id,
        userId = userId,
        title = title,
        imageUri = imageUri,
        rent = rent / 10000,
        deposit = deposit / 10000,
        ageRangeAndGender = ageRangeAndGender,
        isVisible = isVisible,
        areaText = areaText,
        isHidden = !isVisible
    )
}