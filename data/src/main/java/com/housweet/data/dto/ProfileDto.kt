package com.housweet.data.dto

import com.housweet.domain.model.profile.ProfileModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileDto(
    val gender: String,
    val introduce: String,
    val mbti: String,
    val nickname: String,
    @SerialName("profile_image") val profileImage: String,
    val tag: List<String>?,
    @SerialName("user_id") val userId: Int,
    @SerialName("year_of_birth") val yearOfBirth: Int
) {
    fun mapToProfileModel(): ProfileModel {
        return ProfileModel(
            userId = userId,
            nickname = nickname,
            introduce = introduce,
            profileImage = profileImage,
            yearOfBirth = yearOfBirth.toString(),
            gender = genderToKorean(gender),
            mbti = mbti,
            tags = tag ?: emptyList(),
            isCheckedUser = false
        )
    }
    private fun genderToKorean(gender: String): String {
        return when (gender) {
            "MALE", "male" -> "남성"
            "FEMALE", "female" -> "여성"
            else -> ""
        }
    }
}