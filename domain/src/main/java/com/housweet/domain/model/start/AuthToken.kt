package com.housweet.domain.model.start

data class AuthToken(
    val accessToken: String,
    val refreshToken: String,
)