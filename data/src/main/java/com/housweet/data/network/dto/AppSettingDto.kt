package com.housweet.data.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AppSettingResponseDto(
    @SerialName("notification_enabled") val notificationEnabled: Boolean,
    val categories: List<AppSettingCategoryDto>
)

@Serializable
data class AppSettingCategoryDto(
    @SerialName("category_id") val categoryId: Int,
    @SerialName("category_name") val categoryName: String,
    val settings: List<AppSettingItemDto>
)

@Serializable
data class AppSettingItemDto(
    val id: Int,
    @SerialName("template_id") val templateId: Int? = null,
    val key: String,
    @SerialName("display_name") val displayName: String,
    @SerialName("is_enabled") val isEnabled: Boolean
)