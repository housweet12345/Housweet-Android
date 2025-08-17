package com.housweet.data.network.dto

import com.housweet.data.network.PostingDetailDto
import com.housweet.domain.model.RoomPostingDetail

fun PostingDetailDto.toDomain(): RoomPostingDetail =
    RoomPostingDetail(
        title = title.orEmpty(),
        content = content.orEmpty(),
        imageUri = imageUri ?: "",
//        images = images,

        trafficTags = trafficTags ?: emptyList(),
        sizeOfHouseTags = sizeOfHouseTags ?: emptyList(),
        infraTags = infraTags ?: emptyList(),
        lifePatternTags = lifePatternTags ?: emptyList(),
        tidyingUpHabitTags = tidyingUpHabitTags ?: emptyList(),
        personalityTags = personalityTags ?: emptyList(),

        rent = rent ?: 0,
        deposit = deposit ?: 0,
        managementFee = managementFee ?: 0,
        availableFrom = availableFrom.orEmpty(),

        lotNumberAddress = lotNumberAddress,
        roadAddress = roadAddress,
        detailedAddress = detailedAddress,

        si = si ?: 0,
        gu = gu ?: 0,
        dong = dong ?: 0,
        isVisible = isVisible ?: true
    )