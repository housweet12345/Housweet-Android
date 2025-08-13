package com.housweet.data.network

import com.housweet.data.network.dto.IsTermsOfServiceAgreedResponseDto
import com.housweet.data.network.dto.RefreshResponseDto
import io.ktor.client.statement.HttpResponse

interface AuthRemoteDataSource {
    suspend fun loginWithKakao(
        socialId: String,
        accessToken: String,
        email: String
    ): HttpResponse
    suspend fun refreshAccessToken(
        refreshToken: String
    ): RefreshResponseDto
    suspend fun agreeTermsOfService(): Boolean
    suspend fun isTermsOfServiceAgreed(): IsTermsOfServiceAgreedResponseDto
    suspend fun isBelongToRoom(): Boolean
}