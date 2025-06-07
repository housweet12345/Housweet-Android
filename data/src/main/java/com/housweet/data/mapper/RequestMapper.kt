package com.housweet.data.mapper

import com.housweet.data.dto.ProfileUpdateDto
import com.housweet.domain.model.profile.ProfileUpdateModel

fun ProfileUpdateModel.toProfilePatchDto(): ProfileUpdateDto {
    return ProfileUpdateDto(
        gender = gender,
        introduce = introduce,
        mbti = mbti,
        nickname = nickname,
        tags = tags,
        yearOfBirth = yearOfBirth
    )
}