package com.housweet.data.mapper

import com.housweet.data.request.ProfileUpdateRequest
import com.housweet.domain.model.profile.ProfileUpdateModel

fun ProfileUpdateModel.toProfilePatchDto(): ProfileUpdateRequest {
    return ProfileUpdateRequest(
        gender = gender,
        introduce = introduce,
        mbti = mbti,
        nickname = nickname,
        tags = tags,
        yearOfBirth = yearOfBirth
    )
}