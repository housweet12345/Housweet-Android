package com.housweet.domain.model.profile

import kotlinx.serialization.Serializable

@Serializable
data class ProfileUpdateResponseModel(
    val isSuccess: Boolean
)