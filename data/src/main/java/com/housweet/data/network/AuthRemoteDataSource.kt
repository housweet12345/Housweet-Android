package com.housweet.data.network

import com.housweet.data.response.IsTermsOfServiceAgreedResponse
import com.housweet.data.response.RefreshResponse
import io.ktor.client.statement.HttpResponse

interface AuthRemoteDataSource {
    suspend fun loginWithKakao(
        socialId: String,
        accessToken: String,
        email: String
    ): HttpResponse
    suspend fun refreshAccessToken(
        refreshToken: String
    ): RefreshResponse
    suspend fun agreeTermsOfService(): Boolean
    suspend fun isTermsOfServiceAgreed(): IsTermsOfServiceAgreedResponse
    suspend fun isSetProfile(userId: Int): Boolean
    suspend fun isBelongToRoom(): Boolean
    suspend fun deleteAccount(): Boolean
    fun recreateHttpClient()
}