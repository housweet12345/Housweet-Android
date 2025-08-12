package com.housweet.domain.model

data class AppSettingCategory(
    val categoryId: Int,
    val categoryName: String,
    val settings: List<AppSettingItem>
)

data class AppSettingItem(
    val id: Int,
    val templateId: Int?,
    val key: String,
    val displayName: String,
    val isEnabled: Boolean
)