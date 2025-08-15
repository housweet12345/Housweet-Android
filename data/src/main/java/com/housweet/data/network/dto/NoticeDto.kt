package com.housweet.data.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NoticeDto(
    val id: Int,
    val title: String,
    val content: String,
    @SerialName("is_read") val isRead: Boolean,
    @SerialName("created_at") val createdAt: String
)

fun NoticeDto.toDomain() = com.housweet.domain.model.Notice(
    id = id,
    title = title,
    content = content,
    isRead = isRead,
    createdAt = createdAt
)