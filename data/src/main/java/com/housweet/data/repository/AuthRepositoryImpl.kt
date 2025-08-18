package com.housweet.data.repository

import android.util.Log
import com.housweet.data.local.AuthLocalDataSource
import com.housweet.data.network.AuthRemoteDataSource
import com.housweet.data.network.RoomRepository
import com.housweet.data.network.dto.LoginResponseDto
import com.housweet.data.network.dto.toAuthToken
import com.housweet.data.utils.NetworkConnectionManager
import com.housweet.data.utils.NoInternetException
import com.housweet.data.utils.TokenUtils
import com.housweet.domain.local.RoomLocalDataSource
import com.housweet.domain.model.AuthToken
import com.housweet.domain.repository.AuthRepository
import io.ktor.client.call.body
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val authLocalDataSource: AuthLocalDataSource,
    private val authRemoteDataSource: AuthRemoteDataSource,
    private val networkConnectionManager: NetworkConnectionManager,
    private val roomRepository: RoomRepository,
    private val roomLocalDataSource: RoomLocalDataSource
) : AuthRepository {
    override suspend fun loginWithKakao(
        socialId: String,
        accessToken: String,
        email: String
    ): Flow<Result<Boolean>> = flow {
        try {
            val response = authRemoteDataSource.loginWithKakao(
                socialId = socialId,
                accessToken = accessToken,
                email = email
            )
            val responseBody = response.body<LoginResponseDto>()

            val authToken = responseBody.toAuthToken()
            Log.d("TokenCheck", "Housweet accessToken: ${authToken.accessToken}")
            authLocalDataSource.saveAuthToken(authToken)

            emit(Result.success(responseBody.isTermsOfServiceAgreed))

            //로그인 하자마자 roomId 저장
            val roomId = roomRepository.getMyRoomId()
            roomLocalDataSource.saveRoomId(roomId)
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override suspend fun logout() {
        try {
            if (!networkConnectionManager.isInternetAvailable()) {
                throw NoInternetException()
            }
            authLocalDataSource.clearAuthToken()
        } catch (e: Exception) {
            e.printStackTrace()
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
            if (!networkConnectionManager.isInternetAvailable()) {
                throw NoInternetException()
            }

            val isRefreshTokenExpired = authLocalDataSource.isRefreshTokenExpired()
            emit(Result.success(!isRefreshTokenExpired))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override suspend fun agreeTermsOfService(): Flow<Result<Boolean>> = flow {
        try {
            val response = authRemoteDataSource.agreeTermsOfService()
            emit(Result.success(response))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override suspend fun isTermsOfServiceAgreed(): Flow<Result<Boolean>> = flow {
        try {
            val isTermsOfServiceAgreed = authRemoteDataSource.isTermsOfServiceAgreed().termsOfServiceAgreed
            emit(Result.success(isTermsOfServiceAgreed))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override suspend fun isSetProfile(): Flow<Result<Boolean>> = flow {
        try {
            val userId = getCurrentUserId() ?: throw Exception("User ID not found")
            val isSetProfile = authRemoteDataSource.isSetProfile(userId)
            emit(Result.success(isSetProfile))
        } catch (e: Exception) {
            if (e.message.toString().contains("Text: \"<!DOCTYPE html>")) emit(Result.success(false))
            emit(Result.failure(e))
        }
    }

    override suspend fun isBelongToRoom(): Flow<Result<Boolean>> = flow {
        try {
            val isBelongToRoom = authRemoteDataSource.isBelongToRoom()
            emit(Result.success(isBelongToRoom))
        } catch (e: Exception) {
            if (e.message.toString().contains("Room not found")) emit(Result.success(false))
            emit(Result.failure(e))
        }
    }

    override suspend fun getCurrentUserId(): Int? {
        return try {
            val authToken = authLocalDataSource.getAuthToken()
            authToken?.let { token ->
                TokenUtils.getUserIdFromToken(token.accessToken)
            }
        } catch (e: Exception) {
            Log.e("AuthRepository", "Failed to get current user ID", e)
            null
        }
    }
}