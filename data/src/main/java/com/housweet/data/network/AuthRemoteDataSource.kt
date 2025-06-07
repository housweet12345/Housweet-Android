package com.housweet.data.network

import com.housweet.data.network.dto.GeoCodingResponseDto
import com.housweet.data.network.dto.LoginResponseDto
import com.housweet.data.network.dto.TokenResponseDto

interface AuthRemoteDataSource {
    suspend fun geoCodingWithNaver(query: String): GeoCodingResponseDto
    suspend fun loginWithKakao(
        socialId: String,
        accessToken: String,
        email: String
    ): LoginResponseDto
    suspend fun refreshAccessToken(
        refreshToken: String
    ): TokenResponseDto
}