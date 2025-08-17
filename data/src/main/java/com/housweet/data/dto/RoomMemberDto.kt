package com.housweet.data.dto

import com.housweet.domain.model.home.RoomMemberModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RoomMemberDto(
    val id: Int,
    @SerialName("user_id")
    val userId: Int,
    val nickname: String,
    @SerialName("profile_image_url")
    val profileImageUrl: String?,
    val feeling: String,
    @SerialName("joined_at")
    val joinedAt: String? = null
) {
    fun mapToRoomMemberModel(): RoomMemberModel {
        return RoomMemberModel(
            id = id,
            userId = userId,
            nickname = nickname,
            profileImageUrl = profileImageUrl,
            feeling = feeling
        )
    }
}