package com.housweet.domain.repository

import com.housweet.domain.model.AuthToken
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun loginWithKakao(
        socialId: String,
        accessToken: String,
        email: String
    ): Flow<Result<AuthToken>>
}