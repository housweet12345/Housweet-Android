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

    override suspend fun refreshAccessToken(): Flow<Result<AuthToken>> = flow {
        try {
            val refreshToken = authLocalDataSource.getAuthToken()?.refreshToken ?: throw Exception("No refresh token available")
            val response = authRemoteDataSource.refreshAccessToken(refreshToken)
            val newAccessToken = response.accessToken
            authLocalDataSource.saveAuthToken(AuthToken(newAccessToken, refreshToken))
            emit(Result.success(AuthToken(newAccessToken, refreshToken)))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override suspend fun checkLogin(): Flow<Result<Boolean>> = flow {
        try {
            val isRefreshTokenExpired = authLocalDataSource.isRefreshTokenExpired()
            emit(Result.success(!isRefreshTokenExpired))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}