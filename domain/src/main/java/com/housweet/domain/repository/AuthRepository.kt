package com.housweet.domain.repository

import com.housweet.domain.model.start.AuthToken
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun loginWithKakao(
        socialId: String,
        accessToken: String,
        email: String
    ): Flow<Result<Boolean>>
    suspend fun logout()
    suspend fun refreshAccessToken(): Flow<Result<AuthToken>>
    suspend fun checkLogin(): Flow<Result<Boolean>>
    suspend fun agreeTermsOfService(): Flow<Result<Boolean>>
    suspend fun isTermsOfServiceAgreed(): Flow<Result<Boolean>>
    suspend fun isSetProfile(): Flow<Result<Boolean>>
    suspend fun isBelongToRoom(): Flow<Result<Boolean>>
    suspend fun getCurrentUserId(): Int?
    suspend fun deleteAccount(): Flow<Result<Boolean>>
}