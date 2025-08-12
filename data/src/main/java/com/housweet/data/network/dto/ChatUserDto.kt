package com.housweet.data.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class ChatUserDto(
    val id: Int,
    val username: String,
    val email: String
)
