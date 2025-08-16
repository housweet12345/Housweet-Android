package com.housweet.data.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileUpdateDto(
    val gender: String,
    val introduce: String,
    val mbti: String,
    val nickname: String,
    @SerialName("tag")
    val tags: List<String>,
    @SerialName("year_of_birth")
    val yearOfBirth: String
)