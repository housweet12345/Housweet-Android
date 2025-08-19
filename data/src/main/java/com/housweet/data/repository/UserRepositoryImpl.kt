package com.housweet.data.repository

import com.housweet.data.BuildConfig
import com.housweet.data.dto.BlockUserRequestDto
import com.housweet.data.dto.BlockUserResponseDto
import com.housweet.data.mapper.toProfilePatchDto
import com.housweet.data.network.KtorService
import com.housweet.data.network.dto.ProfileDto
import com.housweet.data.network.dto.ProfileUpdateDto
import com.housweet.data.network.dto.ProfileUpdateResponseDto
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
    private val ktorService: KtorService
): UserRepository {

    private val client: HttpClient
        get() = ktorService.getHttpClient()

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

    override suspend fun updateProfile(userId: String, updatedProfile: ProfileUpdateModel): Result<ProfileUpdateResponseModel> {
        return runCatching {
            val response: ProfileUpdateResponseDto = if (updatedProfile.profileImageData != null) {
                // 이미지가 있으면 멀티파트로 전송
                val formData = formData {
                    appendProfileData(updatedProfile)
                }
                
                client.patch("${BuildConfig.USER_BASE_URL}/profile/$userId/update/") {
                    setBody(MultiPartFormDataContent(formData))
                }.body()
            } else {
                // 이미지가 없으면 일반 JSON으로 전송
                val patchDto: ProfileUpdateDto = updatedProfile.toProfilePatchDto()
                client.patch("${BuildConfig.USER_BASE_URL}/profile/$userId/update/") {
                    setBody(patchDto)
                }.body()
            }
            response.mapToProfileUpdateResponseModel()
        }
    }

    override suspend fun blockUser(blockedUserId: Int): Result<Boolean> {
        return runCatching {
            val requestDto = BlockUserRequestDto(blockedUserId = blockedUserId)
            val response: BlockUserResponseDto = client.post("${BuildConfig.BASE_URL}/room/room-postings/block/") {
                setBody(requestDto)
            }.body()
            response.detail.contains("successfully", ignoreCase = true)
        }
    }
}