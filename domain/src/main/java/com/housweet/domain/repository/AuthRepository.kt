package com.housweet.domain.repository

import com.housweet.domain.model.start.AuthToken

interface AuthRepository {
    suspend fun loginWithKakao(
        socialId: String,
        accessToken: String,
        email: String
    ): Result<Boolean>
    suspend fun logout()
    suspend fun refreshAccessToken(): Result<AuthToken>
    suspend fun checkLogin(): Result<Boolean>
    suspend fun agreeTermsOfService(): Result<Boolean>
    suspend fun isTermsOfServiceAgreed(): Result<Boolean>
    suspend fun isSetProfile(): Result<Boolean>
    suspend fun isBelongToRoom(): Result<Boolean>
    suspend fun getCurrentUserId(): Int?
    suspend fun deleteAccount(): Result<Boolean>
}