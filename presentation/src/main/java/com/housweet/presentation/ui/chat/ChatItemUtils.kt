package com.housweet.presentation.ui.chat

import com.housweet.domain.model.ChatMessage
import com.housweet.presentation.R
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

fun processChatMessagesWithDate(
    messages: List<ChatMessage>,
    senderId: Int
): List<ChatItem> {
    val formatter = DateTimeFormatter.ofPattern("M월 d일", Locale.KOREAN)
    val result = mutableListOf<ChatItem>()

    var lastDate: String? = null

    for (msg in messages) {
        val zdt = ZonedDateTime.parse(msg.createdAt)
        val currentDate = zdt.format(formatter)

        if (currentDate != lastDate) {
            result.add(ChatItem.DateHeader(currentDate)) // 날짜 헤더 삽입
            lastDate = currentDate
        }

        result.add(
            ChatItem.TextMessage(
                message = msg.content,
                isMine = msg.senderId == senderId,
                profileImageRes = if (msg.senderId != senderId) R.drawable.default_profile else null
            )
        )
    }

    return result
}