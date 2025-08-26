package com.housweet.data.response

import com.housweet.domain.model.profile.ProfileUpdateResponseModel
import kotlinx.serialization.Serializable

@Serializable
data class ProfileUpdateResponse(
    val ok: Boolean
) {
    fun mapToProfileUpdateResponseModel(): ProfileUpdateResponseModel {
        return ProfileUpdateResponseModel(
            isSuccess = ok
        )
    }
}