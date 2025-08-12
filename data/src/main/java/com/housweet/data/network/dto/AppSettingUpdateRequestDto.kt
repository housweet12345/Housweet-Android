package com.housweet.data.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class AppSettingUpdateRequestDto(
    val is_enabled: Boolean
)