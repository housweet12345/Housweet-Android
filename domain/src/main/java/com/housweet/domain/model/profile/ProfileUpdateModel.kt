package com.housweet.domain.model.profile

data class ProfileUpdateModel(
    val gender: String,
    val introduce: String,
    val mbti: String,
    val nickname: String,
    val tags: List<String>,
    val yearOfBirth: String
)