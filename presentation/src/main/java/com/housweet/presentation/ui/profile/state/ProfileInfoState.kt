package com.housweet.presentation.ui.profile.state

import com.housweet.domain.model.profile.ProfileModel
import java.util.Calendar

sealed interface ProfileInfoState {
    object Loading : ProfileInfoState
    data class Success(val profileInfo: ProfileInfo) : ProfileInfoState
    data class Error(val message: String) : ProfileInfoState
}

data class ProfileInfo(
    val userId: Int = 0,
    val gender: String = "",
    val introduce: String = "",
    val isCheckedUser: Boolean = false,
    val mbti: String = "",
    val nickname: String = "",
    val profileImageUrl: String = "",
    val tags: List<String> = emptyList(),
    val myProfile: Boolean = false,
    val age: String = ""
)

fun ProfileModel.toProfileInfo(isMyProfile: Boolean): ProfileInfo {
    return ProfileInfo(
        userId = userId,
        gender = gender,
        introduce = introduce,
        isCheckedUser = isCheckedUser,
        mbti = mbti,
        nickname = nickname,
        profileImageUrl = profileImage,
        tags = listOf(mbti) + tags,
        myProfile = isMyProfile,
        age = getAgeGroupFromBirthYear(yearOfBirth)
    )
}

private fun getAgeGroupFromBirthYear(yearOfBirth: String): String {
    val birthYear = yearOfBirth.toIntOrNull() ?: return "알 수 없음"
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    val age = currentYear - birthYear

    return when {
        age < 10 -> "10세 미만"
        age < 100 -> "${(age / 10) * 10}대"
        else -> "100세 이상"
    }
}