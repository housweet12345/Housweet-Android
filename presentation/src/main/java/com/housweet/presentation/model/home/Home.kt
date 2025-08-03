package com.housweet.presentation.model.home

import com.housweet.presentation.model.schedule.TodoInfo

data class HomeInfo(
    val roomName: String = "",
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

enum class MoodType(val displayName: String) {
    HAPPY("행복"),
    NORMAL("무난"),
    SAD("슬픔"),
    ANGRY("화남"),
    LOVE("애정"),
    CONGRAT("축하"),
    OUTSIDE("외출")
}