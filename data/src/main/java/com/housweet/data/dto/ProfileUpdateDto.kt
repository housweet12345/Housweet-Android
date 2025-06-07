package com.housweet.data.dto

import com.housweet.domain.model.profile.ProfileUpdateModel

data class ProfileUpdateDto(
    val gender: String,
    val introduce: String,
    val mbti: String,
    val nickname: String,
    val tags: List<String>,
    val yearOfBirth: String
) {
    fun mapToProfileUpdateModel(): ProfileUpdateModel {
        return ProfileUpdateModel(
            gender = gender,
            introduce = introduce,
            mbti = mbti,
            nickname = nickname,
            tags = tags,
            yearOfBirth = yearOfBirth
        )
    }
}