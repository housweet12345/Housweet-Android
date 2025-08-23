package com.housweet.data.request

import kotlinx.serialization.Serializable

@Serializable
data class AppSettingUpdateRequest(
    val is_enabled: Boolean
)