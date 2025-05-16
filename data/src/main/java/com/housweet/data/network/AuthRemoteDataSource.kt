package com.housweet.data.network

import com.housweet.data.network.dto.LoginResponseDto

interface AuthRemoteDataSource {
    suspend fun loginWithKakao(
        socialId: String,
        accessToken: String,
        email: String
    ): LoginResponseDto
}