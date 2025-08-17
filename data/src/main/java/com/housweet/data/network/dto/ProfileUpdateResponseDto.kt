package com.housweet.data.network.dto

import com.housweet.domain.model.profile.ProfileUpdateResponseModel
import kotlinx.serialization.Serializable

@Serializable
data class ProfileUpdateResponseDto(
    val ok: Boolean
) {
    fun mapToProfileUpdateResponseModel(): ProfileUpdateResponseModel {
        return ProfileUpdateResponseModel(
            isSuccess = ok
        )
    }
}