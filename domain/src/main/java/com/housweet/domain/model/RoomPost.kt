package com.housweet.domain.model

import com.squareup.moshi.Json

data class RoomPost(
    val id: Int, // 고유 ID
    val title: String,

    // 원시 데이터 (백엔드 응답 그대로)
    val deposit: String,
    val rent: String,
    val ageRangeAndGender: String,
    val imageUrl: String?,

    // UI에 필요한 가공 데이터
    val priceInfo: String,
    val metaInfo: String,

    var isHidden: Boolean = false, // 생성자 파라미터로 변경 (val로 해도 무방)
    val userId: Int, // 추가: 게시글 작성자 ID
)
