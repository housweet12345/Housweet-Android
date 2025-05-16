package com.housweet.data.repository

import com.housweet.data.local.AuthLocalDataSource
import com.housweet.data.network.AuthRemoteDataSource
import com.housweet.data.network.dto.toAuthToken
import com.housweet.domain.model.AuthToken
import com.housweet.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val authLocalDataSource: AuthLocalDataSource,
    private val authRemoteDataSource: AuthRemoteDataSource
) : AuthRepository {
    override suspend fun loginWithKakao(
        socialId: String,
        accessToken: String,
        email: String
    ): Flow<Result<AuthToken>> = flow {
        try {
            val response = authRemoteDataSource.loginWithKakao(
                socialId = socialId,
                accessToken = accessToken,
                email = email
            )
            val authToken = response.toAuthToken()
            authLocalDataSource.saveAuthToken(authToken)
            emit(Result.success(authToken))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}