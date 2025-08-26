package com.housweet.data.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NoticeResponse(
    val id: Int,
    val title: String,
    val content: String,
    @SerialName("is_read") val isRead: Boolean,
    @SerialName("created_at") val createdAt: String
)

fun NoticeResponse.toDomain() = com.housweet.domain.model.Notice(
    id = id,
    title = title,
    content = content,
    isRead = isRead,
    createdAt = createdAt
)