package com.housweet.domain.model

//채팅목록에 표시될 간단한 모델 (이름, 메세지, 시간 등)
data class ChatPreview (
    val name: String,
    val lastMessage: String? = null,
    val time: String? = null,
    val profileImageUrl: String? = null,
    val unread: Boolean = false
)