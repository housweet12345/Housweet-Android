package com.housweet.domain.model.community

data class RoomPostDetailDataModel(
    val id: Int = 0,
    val userId: Int = -1,
    val title: String = "오류",
    val content: String = "오류",
    val imageUri: String = "",
    val rent: Int = 9999,
    val deposit: Int = 9999,
    val managementFee: Int = 9999,
    val availableFrom: String = "오류",
    val ageRangeAndGender: String = "오류",
    val isBookmarked: Boolean = false,
    val lotNumberAddress: String = "오류",
    val roadAddress: String = "오류",
    val detailedAddress: String = "오류",
    val areaText: String = "오류",
    val trafficTags: List<String> = listOf(),
    val sizeOfHouseTags: List<String> = listOf(),
    val infraTags: List<String> = listOf(),
    val lifePatternTags: List<String> = listOf(),
    val tidyingUpHabitTags: List<String> = listOf(),
    val personalityTags: List<String> = listOf(),
    val nickName: String = "오류",
    val profileImageUrl: String = "",
    val createdAtKst: String = ""
)