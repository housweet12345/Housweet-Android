package com.housweet.domain.model

data class Notice(
    val id: Int,
    val title: String,
    val content: String,
    val isRead: Boolean,
    val createdAt: String
)
