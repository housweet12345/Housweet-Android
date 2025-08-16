package com.housweet.presentation.ui.userlist.state

import com.housweet.domain.model.home.RoomMemberModel
import com.housweet.presentation.ui.home.state.MoodType

data class UserItem(
    val id: String,
    val userId: Int,
    val name: String,
    val profileImageUrl: String? = null,
    val mood: MoodType = MoodType.NORMAL,
    val isHost: Boolean = false // 방장 여부
)

fun RoomMemberModel.toUserItem(): UserItem {
    return UserItem(
        id = id.toString(),
        userId = userId,
        name = nickname,
        profileImageUrl = profileImageUrl,
        mood = mapStringToMoodType(feeling)
    )
}

private fun mapStringToMoodType(feeling: String): MoodType {
    return when (feeling.lowercase()) {
        "happy" -> MoodType.HAPPY
        "normal" -> MoodType.NORMAL
        "sad" -> MoodType.SAD
        "angry" -> MoodType.ANGRY
        "love" -> MoodType.LOVE
        "celebrate" -> MoodType.CELEBRATE
        "away" -> MoodType.AWAY
        else -> MoodType.NORMAL // 기본값
    }
}