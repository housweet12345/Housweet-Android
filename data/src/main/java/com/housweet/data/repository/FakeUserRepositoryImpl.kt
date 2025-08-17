package com.housweet.data.repository

import android.util.Log
import com.housweet.domain.model.profile.ProfileModel
import com.housweet.domain.model.profile.ProfileUpdateModel
import com.housweet.domain.model.profile.ProfileUpdateResponseModel
import com.housweet.domain.repository.UserRepository
import kotlinx.coroutines.delay

class FakeUserRepositoryImpl : UserRepository {

    // Fake 데이터
    private val fakeProfileModel = ProfileModel(
        userId = 1,
        nickname = "테스트유저",
        introduce = "안녕하세요! 테스트 사용자입니다.",
        profileImage = "https://example.com/profile.jpg",
        yearOfBirth = "1990",
        gender = "남자",
        mbti = "ENFP",
        tags = listOf("개발자", "안드로이드", "코틀린"),
        isCheckedUser = true
    )

    private val fakeProfileUpdateResponseModel = ProfileUpdateResponseModel(
        isSuccess = true
    )

    override suspend fun getMyProfile(): Result<ProfileModel> {
        // 네트워크 지연 시뮬레이션
        delay(1000)
        return Result.success(fakeProfileModel)
    }

    override suspend fun getOtherUserProfile(userId: String): Result<ProfileModel> {
        delay(500)
        
        // 특정 조건에서 에러 시뮬레이션
        if (userId == "error") {
            return Result.failure(Exception("사용자를 찾을 수 없습니다"))
        }
        
        return Result.success(
            fakeProfileModel.copy(
                userId = userId.toIntOrNull() ?: 999,
                nickname = "다른사용자$userId"
            )
        )
    }

    override suspend fun updateProfile(userId: String, updatedProfile: ProfileUpdateModel): Result<ProfileUpdateResponseModel> {
        delay(800)
        Log.i("updateProfile", "userId: $userId, updatedProfile: $updatedProfile")
        return Result.success(fakeProfileUpdateResponseModel)
    }

    override suspend fun blockUser(blockedUserId: Int): Result<Boolean> {
        delay(500)
        Log.i("blockUser", "blockedUserId: $blockedUserId")
        return Result.success(true)
    }
}