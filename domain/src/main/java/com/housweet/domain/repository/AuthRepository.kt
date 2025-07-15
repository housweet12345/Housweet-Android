package com.housweet.domain.repository

import com.housweet.domain.model.AuthToken
import com.housweet.domain.model.Coordinate
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun loginWithKakao(
        socialId: String,
        accessToken: String,
        email: String
    ): Flow<Result<Int>>

    suspend fun refreshAccessToken(): Flow<Result<AuthToken>>

    suspend fun checkLogin(): Flow<Result<Boolean>>
    suspend fun geoCodingWithNaver(query: String): Flow<Result<Coordinate>>
}