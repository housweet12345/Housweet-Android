package com.housweet.data.repository

import android.util.Log
import com.housweet.data.local.AuthLocalDataSource
import com.housweet.data.datasource.AuthRemoteDataSource
import com.housweet.data.response.LoginResponse
import com.housweet.data.response.toAuthToken
import com.housweet.data.utils.NetworkConnectionManager
import com.housweet.data.utils.NoInternetException
import com.housweet.data.utils.TokenUtils
import com.housweet.domain.local.RoomLocalDataSource
import com.housweet.domain.model.start.AuthToken
import com.housweet.domain.repository.AuthRepository
import io.ktor.client.call.body
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
    ): Result<Boolean> {
        return try {
            val response = authRemoteDataSource.loginWithKakao(
                socialId = socialId,
                accessToken = accessToken,
                email = email
            )
            val responseBody = response.body<LoginResponse>()

            val authToken = responseBody.toAuthToken()
            Log.d("TokenCheck", "Housweet accessToken: ${authToken.accessToken}")
            authLocalDataSource.saveAuthToken(authToken)

            //로그인 하자마자 roomId 저장
            val roomId = roomRepository.getMyRoomId()
            roomLocalDataSource.saveRoomId(roomId)
            
            Result.success(responseBody.isTermsOfServiceAgreed)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun logout() {
        try {
            if (!networkConnectionManager.isInternetAvailable()) {
                throw NoInternetException()
            }
            authLocalDataSource.clearAuthToken()
            authRemoteDataSource.recreateHttpClient()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun refreshAccessToken(): Result<AuthToken> {
        return try {
            val refreshToken = authLocalDataSource.getAuthToken()?.refreshToken ?: throw Exception("No refresh token available")
            val response = authRemoteDataSource.refreshAccessToken(refreshToken)
            val newAccessToken = response.accessToken
            authLocalDataSource.saveAuthToken(AuthToken(newAccessToken, refreshToken))
            Result.success(AuthToken(newAccessToken, refreshToken))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun checkLogin(): Result<Boolean> {
        return try {
            if (!networkConnectionManager.isInternetAvailable()) {
                throw NoInternetException()
            }

            val isRefreshTokenExpired = authLocalDataSource.isRefreshTokenExpired()
            Result.success(!isRefreshTokenExpired)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun agreeTermsOfService(): Result<Boolean> {
        return try {
            val response = authRemoteDataSource.agreeTermsOfService()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun isTermsOfServiceAgreed(): Result<Boolean> {
        return try {
            val isTermsOfServiceAgreed = authRemoteDataSource.isTermsOfServiceAgreed().termsOfServiceAgreed
            Result.success(isTermsOfServiceAgreed)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun isSetProfile(): Result<Boolean> {
        return try {
            val userId = getCurrentUserId() ?: throw Exception("User ID not found")
            val isSetProfile = authRemoteDataSource.isSetProfile(userId)
            Result.success(isSetProfile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun isBelongToRoom(): Result<Boolean> {
        return try {
            val isBelongToRoom = authRemoteDataSource.isBelongToRoom()
            Result.success(isBelongToRoom)
        } catch (e: Exception) {
            if (e.message.toString().contains("Room not found")) Result.success(false)
            else Result.failure(e)
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

    override suspend fun deleteAccount(): Result<Boolean> {
        return try {
            val successDeleteAccount = authRemoteDataSource.deleteAccount()
            if (successDeleteAccount) {
                authLocalDataSource.clearAuthToken()
            }

            Result.success(successDeleteAccount)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}