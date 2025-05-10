package com.housweet.domain.model

//채팅목록에 표시될 간단한 모델 (이름, 메세지, 시간 등)
data class ChatPreview (
    val name: String,
    val lastMessage: String,
    val time: String,
    val unread: Boolean
)

val dummyChatList = listOf(
    ChatPreview("김지안", "집을 문의하고 싶어서 연락드렸어요. 지금...", "오전 11:30", true),
    ChatPreview("김지안", "안녕하세요", "오전 11:30", false),
    ChatPreview("김지안", "안녕하세요", "오전 11:30", false)
)