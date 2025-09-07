package com.housweet.data.response

import com.housweet.domain.model.NotificationModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NotificationResponse(
    val id: Int,
    val type: String,
    val content: String,
    @SerialName("created_at") val createdAt: String
)

fun NotificationResponse.toDomain(): NotificationModel {
    return NotificationModel(
        id = id,
        type = type,
        content = content,
        createdAt = createdAt
    )
}