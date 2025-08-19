package com.housweet.domain.model

data class RoomPostingDetail(
    val title: String = "",
    val content: String = "",
    val imageUri: String? = null,
    val trafficTags: List<String> = emptyList(),
    val sizeOfHouseTags: List<String> = emptyList(),
    val infraTags: List<String> = emptyList(),
    val lifePatternTags: List<String> = emptyList(),
    val tidyingUpHabitTags: List<String> = emptyList(),
    val personalityTags: List<String> = emptyList(),
    val rent: Int = 0,
    val deposit: Int = 0,
    val managementFee: Int = 0,
    val availableFrom: String = "",
    val lotNumberAddress: String? = null,
    val roadAddress: String? = null,
    val detailedAddress: String? = null,
    val si: String? = null,
    val gu: String? = null,
    val dong: String? = null,
    val isVisible: Boolean = true
)
