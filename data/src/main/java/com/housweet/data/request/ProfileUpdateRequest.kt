package com.housweet.data.request

import com.housweet.domain.model.profile.ProfileUpdateModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileUpdateRequest(
    val gender: String,
    val introduce: String,
    val mbti: String,
    val nickname: String,
    @SerialName("tag")
    val tags: List<String>,
    @SerialName("year_of_birth")
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