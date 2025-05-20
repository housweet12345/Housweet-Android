package com.housweet.data.dto

import com.housweet.domain.model.profile.ProfileModel

data class ProfileDto(
    val gender: String,
    val introduce: String,
    val isCheckedUser: Boolean,
    val mbti: String,
    val nickname: String,
    val profileImage: String,
    val tags: List<String>,
    val userId: Int,
    val yearOfBirth: String
) {
    fun mapToProfileModel(): ProfileModel {
        return ProfileModel(
            userId = userId,
            nickname = nickname,
            introduce = introduce,
            profileImage = profileImage,
            yearOfBirth = yearOfBirth,
            gender = gender,
            mbti = mbti,
            tags = tags,
            isCheckedUser = isCheckedUser
        )
    }
}