package com.housweet.data.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AppSettingResponse(
    @SerialName("notification_enabled") val notificationEnabled: Boolean,
    val categories: List<AppSettingCategoryResponse>
)

@Serializable
data class AppSettingCategoryResponse(
    @SerialName("category_id") val categoryId: Int,
    @SerialName("category_name") val categoryName: String,
    val settings: List<AppSettingItemResponse>
)

@Serializable
data class AppSettingItemResponse(
    val id: Int,
    @SerialName("template_id") val templateId: Int? = null,
    val key: String,
    @SerialName("display_name") val displayName: String,
    @SerialName("is_enabled") val isEnabled: Boolean
)