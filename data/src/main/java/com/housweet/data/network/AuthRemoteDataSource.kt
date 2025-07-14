package com.housweet.data.network

import com.housweet.data.network.dto.IsTermsOfServiceAgreedResponseDto
import com.housweet.data.network.dto.TokenResponseDto
import io.ktor.client.statement.HttpResponse

interface AuthRemoteDataSource {
    suspend fun loginWithKakao(
        socialId: String,
        accessToken: String,
        email: String
    ): HttpResponse
    suspend fun refreshAccessToken(
        refreshToken: String
    ): TokenResponseDto
    suspend fun agreeTermsOfService() : Boolean
    suspend fun isTermsOfServiceAgreed(): IsTermsOfServiceAgreedResponseDto
}