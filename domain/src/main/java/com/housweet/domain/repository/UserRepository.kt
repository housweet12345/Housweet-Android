package com.housweet.domain.repository

import com.housweet.domain.model.profile.ProfileModel
import com.housweet.domain.model.profile.ProfileUpdateModel
import com.housweet.domain.model.profile.ProfileUpdateResponseModel

interface UserRepository {
    suspend fun getMyProfile(): Result<ProfileModel>
    suspend fun getOtherUserProfile(userId: String): Result<ProfileModel>
    suspend fun updateProfile(userId: String, updatedProfile: ProfileUpdateModel): Result<ProfileUpdateResponseModel>
}