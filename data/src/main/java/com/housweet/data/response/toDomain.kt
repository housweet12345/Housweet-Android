package com.housweet.data.response

import com.housweet.domain.model.RoomPostingDetail

fun PostingDetailResponse.toDomain(): RoomPostingDetail =
    RoomPostingDetail(
        title = title.orEmpty(),
        content = content.orEmpty(),
        imageUri = imageUri ?: "",
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
        lotNumberAddress = lotNumberAddress.orEmpty(),
        roadAddress = roadAddress.orEmpty(),
        detailedAddress = detailedAddress.orEmpty(),
        si = si.orEmpty(),
        gu = gu.orEmpty(),
        dong = dong.orEmpty(),
        isVisible = isVisible ?: false
    )