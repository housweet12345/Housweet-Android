package com.housweet.domain.model

data class RoomPost(
    val id: Int, // 고유 ID
    val userId: Int,

    val title: String,
    val imageUri: String?,
    val rent: Int,
    val deposit: Int,
    val ageRangeAndGender: String,
    val isVisible: Boolean,
    val areaText: String?,

    // UI에 필요한 가공 데이터
    var isHidden: Boolean = false, // 생성자 파라미터로 변경 (val로 해도 무방)
)
