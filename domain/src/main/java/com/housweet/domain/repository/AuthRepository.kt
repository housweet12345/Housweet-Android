package com.housweet.domain.repository

import com.housweet.domain.model.AuthToken
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun loginWithKakao(kakaoToken: String): Flow<Result<AuthToken>>
    suspend fun test(): Flow<Result<AuthToken>>
}