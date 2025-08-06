package com.housweet.data.network.dto

import com.housweet.domain.model.NotificationModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NotificationDto(
    val id: Int,
    val type: String,
    val content: String,
    @SerialName("created_at") val createdAt: String
)

fun NotificationDto.toDomain(): NotificationModel {
    return NotificationModel(
        id = id,
        type = type,
        content = content,
        createdAt = createdAt
    )
}