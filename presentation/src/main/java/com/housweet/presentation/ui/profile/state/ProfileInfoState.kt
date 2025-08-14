package com.housweet.presentation.ui.profile.state

import com.housweet.domain.model.profile.ProfileModel
import java.util.Calendar

// This ReportResult is not used anymore, it's moved to ProfileInfoViewModel
// sealed class ReportResult {
//     data class Success(val data: Boolean) : ReportResult()
//     data class Error(val exception: Throwable) : ReportResult()
// }

sealed interface ProfileInfoState {
    data object Loading : ProfileInfoState
    data object EditSuccess : ProfileInfoState
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
    val yearOfBirth: String = "",
    val age: String = "",
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
        yearOfBirth = yearOfBirth,
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