package com.housweet.data.repository

import com.housweet.data.BuildConfig
import com.housweet.data.request.BlockUserRequest
import com.housweet.data.response.BlockUserResponse
import com.housweet.data.local.AuthLocalDataSource
import com.housweet.data.mapper.toProfilePatchDto
import com.housweet.data.network.KtorService
import com.housweet.data.response.ProfileResponse
import com.housweet.data.request.ProfileUpdateRequest
import com.housweet.data.response.ProfileUpdateResponse
import com.housweet.data.utils.TokenUtils
import com.housweet.data.utils.appendProfileData
import com.housweet.domain.model.profile.ProfileModel
import com.housweet.domain.model.profile.ProfileUpdateModel
import com.housweet.domain.model.profile.ProfileUpdateResponseModel
import com.housweet.domain.repository.UserRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val ktorService: KtorService,
    private val authLocalDataSource: AuthLocalDataSource,
): UserRepository {

    private val client: HttpClient
        get() = ktorService.getHttpClient()

    override suspend fun getMyProfile(): Result<ProfileModel> {
        return runCatching {
            val response: ProfileResponse = client.get("${BuildConfig.USER_BASE_URL}/profile/me").body()
            response.mapToProfileModel()
        }
    }

    override suspend fun getOtherUserProfile(userId: String): Result<ProfileModel> {

        val currentUserId = authLocalDataSource.getAuthToken()?.let { token ->
            TokenUtils.getUserIdFromToken(token.accessToken)
        } ?: ""

        return runCatching {
            val response: ProfileResponse = client.get("${BuildConfig.USER_BASE_URL}/profile/$currentUserId/$userId").body()
            response.mapToProfileModel()
        }
    }

    override suspend fun updateProfile(userId: String, updatedProfile: ProfileUpdateModel): Result<ProfileUpdateResponseModel> {
        return runCatching {
            val response: ProfileUpdateResponse = if (updatedProfile.profileImageData != null) {
                // 이미지가 있으면 멀티파트로 전송
                val formData = formData {
                    appendProfileData(updatedProfile)
                }
                
                client.patch("${BuildConfig.USER_BASE_URL}/profile/$userId/update/") {
                    setBody(MultiPartFormDataContent(formData))
                }.body()
            } else {
                // 이미지가 없으면 일반 JSON으로 전송
                val patchDto: ProfileUpdateRequest = updatedProfile.toProfilePatchDto()
                client.patch("${BuildConfig.USER_BASE_URL}/profile/$userId/update/") {
                    setBody(patchDto)
                }.body()
            }
            response.mapToProfileUpdateResponseModel()
        }
    }

    override suspend fun blockUser(blockedUserId: Int): Result<Boolean> {
        return runCatching {
            val requestDto = BlockUserRequest(blockedUserId = blockedUserId)
            val response: BlockUserResponse = client.post("${BuildConfig.BASE_URL}/room/room-postings/block/") {
                setBody(requestDto)
            }.body()
            response.detail.contains("successfully", ignoreCase = true)
        }
    }
}