package com.housweet.presentation.ui.home.state

sealed interface HomeState {
    data object Loading : HomeState
    data class Success(val homeInfo: HomeInfo) : HomeState
    data class Error(val message: String) : HomeState
}

data class HomeInfo(
    val roomName: String = "방이름",
    val daysLiving: Int = 0,
    val notices: List<NoticeItem> = emptyList(),
    val roommates: List<RoommateInfo> = emptyList(),
    val todos: List<TodoInfo> = emptyList()
)

data class NoticeItem(
    val id: Int,
    val title: String,
    val content: String,
    val createdAt: String
)

data class RoommateInfo(
    val userId: Int,
    val nickname: String,
    val profileImageUrl: String,
    val mood: MoodType
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
    CONGRAT("축하"),
    OUTSIDE("외출")
}