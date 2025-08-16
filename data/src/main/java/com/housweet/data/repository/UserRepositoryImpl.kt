package com.housweet.data.repository

import com.housweet.data.BuildConfig
import com.housweet.data.dto.ProfileDto
import com.housweet.data.dto.ProfileUpdateDto
import com.housweet.data.mapper.toProfilePatchDto
import com.housweet.data.network.KtorService
import com.housweet.domain.model.profile.ProfileModel
import com.housweet.domain.model.profile.ProfileUpdateModel
import com.housweet.domain.repository.UserRepository
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.setBody
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val ktorService: KtorService
): UserRepository {

    private val client by lazy { ktorService.createHttpClient() }

    override suspend fun getMyProfile(): Result<ProfileModel> {
        return runCatching {
            val response: ProfileDto = client.get("${BuildConfig.USER_BASE_URL}/profile/me").body()
            response.mapToProfileModel()
        }
    }

    override suspend fun getOtherUserProfile(userId: String): Result<ProfileModel> {
        return runCatching {
            val response: ProfileDto = client.get("${BuildConfig.USER_BASE_URL}/profile/$userId").body()
            response.mapToProfileModel()
        }
    }

    override suspend fun updateProfile(userId: String, updatedProfile: ProfileUpdateModel): Result<ProfileUpdateModel> {
        return runCatching {
            val patchDto = updatedProfile.toProfilePatchDto()
            val response: ProfileUpdateDto = client.patch("${BuildConfig.USER_BASE_URL}/profile/$userId/update") {
                setBody(patchDto)
            }.body()
            response.mapToProfileUpdateModel()
        }
    }
}