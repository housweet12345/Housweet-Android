package com.housweet.domain.repository

import com.housweet.domain.model.AuthToken
import com.housweet.domain.model.Coordinate
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun loginWithKakao(kakaoToken: String): Flow<Result<AuthToken>>
    suspend fun geoCodingWithNaver(query: String): Flow<Result<Coordinate>>
}