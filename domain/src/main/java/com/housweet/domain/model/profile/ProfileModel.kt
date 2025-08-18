package com.housweet.domain.model.profile

data class ProfileModel(
    val gender: String,
    val introduce: String,
    val isCheckedUser: Boolean,
    val mbti: String,
    val nickname: String,
    val profileImage: String,
    val tags: List<String>,
    val userId: Int,
    val yearOfBirth: String,
    val isBlockedUser: Boolean? = null
)