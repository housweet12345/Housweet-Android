package com.housweet.data.network.dto

import com.housweet.domain.model.profile.ProfileModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileDto(
    val gender: String,
    val introduce: String?,
    val mbti: String,
    val nickname: String,
    @SerialName("profile_image") val profileImage: String?,
    val tag: List<String>?,
    @SerialName("user_id") val userId: Int,
    @SerialName("year_of_birth") val yearOfBirth: Int,
    @SerialName("is_blocked") val isBlockedUser: Int? = null
) {
    fun mapToProfileModel(): ProfileModel {
        return ProfileModel(
            userId = userId,
            nickname = nickname,
            introduce = introduce ?: "",
            profileImage = profileImage ?: "",
            yearOfBirth = if (yearOfBirth == 0) "" else yearOfBirth.toString(),
            gender = if (gender == "unknown") "" else genderToKorean(gender),
            mbti = mbti,
            tags = tag ?: emptyList(),
            isCheckedUser = false,
            isBlockedUser = isBlockedUser == 1
        )
    }
    private fun genderToKorean(gender: String): String {
        return when (gender) {
            "MALE", "male", "M" -> "남성"
            "FEMALE", "female", "F" -> "여성"
            "남자", "남성" -> "남성"
            "여자", "여성" -> "여성"
            else -> gender
        }
    }
}