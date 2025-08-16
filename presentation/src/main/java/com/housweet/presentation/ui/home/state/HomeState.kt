package com.housweet.presentation.ui.home.state

import com.housweet.domain.model.home.RoomHomeModel
import com.housweet.domain.model.home.RoomMemberModel

sealed interface HomeState {
    data object Loading : HomeState
    data class Success(val homeInfo: HomeInfo) : HomeState
    data class Error(val message: String) : HomeState
}

data class HomeInfo(
    val roomId: Int = 0,
    val roomName: String = "방이름",
    val daysTogether: Int = 0,
    val members: List<RoommateInfo> = emptyList(),
    val notices: List<NoticeItem> = emptyList(),
    val todos: List<TodoInfo> = emptyList()
)

data class NoticeItem(
    val id: Int,
    val title: String,
    val content: String,
    val createdAt: String
)

data class RoommateInfo(
    val id: Int,
    val userId: Int,
    val nickname: String,
    val profileImageUrl: String?,
    val mood: MoodType,
    val isMe: Boolean = false
)

data class TodoInfo(
    val id: Int,
    val title: String,
    val isCompleted: Boolean,
    val assignedUserId: Int
)

enum class MoodType(val displayName: String) {
    HAPPY("행복"),
    NORMAL("무난"),
    SAD("슬픔"),
    ANGRY("화남"),
    LOVE("애정"),
    CELEBRATE("축하"),
    AWAY("외출")
}

fun RoomHomeModel.toHomeInfo(currentUserId: Int? = null): HomeInfo {
    return HomeInfo(
        roomId = roomId,
        roomName = roomName,
        daysTogether = daysTogether,
        members = members.map { it.toRoommateInfo(currentUserId) }
    )
}

fun RoomMemberModel.toRoommateInfo(currentUserId: Int? = null): RoommateInfo {
    return RoommateInfo(
        id = id,
        userId = userId,
        nickname = nickname,
        profileImageUrl = profileImageUrl,
        mood = mapStringToMoodType(feeling),
        isMe = currentUserId?.let { it == userId } ?: false
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

fun mapMoodTypeToString(moodType: MoodType): String {
    return when (moodType) {
        MoodType.HAPPY -> "happy"
        MoodType.NORMAL -> "normal"
        MoodType.SAD -> "sad"
        MoodType.ANGRY -> "angry"
        MoodType.LOVE -> "love"
        MoodType.CELEBRATE -> "celebrate"
        MoodType.AWAY -> "away"
    }
}