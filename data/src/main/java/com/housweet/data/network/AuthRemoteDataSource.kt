package com.housweet.data.network

import com.housweet.data.network.dto.GeoCodingResponseDto
import com.housweet.data.network.dto.LoginResponseDto
import com.housweet.data.network.dto.TokenResponseDto
import io.ktor.client.statement.HttpResponse

interface AuthRemoteDataSource {
    suspend fun geoCodingWithNaver(query: String): GeoCodingResponseDto
    suspend fun loginWithKakao(
        socialId: String,
        accessToken: String,
        email: String
    ): HttpResponse

    suspend fun refreshAccessToken(
        refreshToken: String
    ): TokenResponseDto
}