package com.housweet.data.repository

import android.util.Log
import com.housweet.data.local.AuthLocalDataSource
import com.housweet.domain.local.RoomLocalDataSource
import com.housweet.data.network.AuthRemoteDataSource
import com.housweet.data.network.RoomRepository
import com.housweet.data.network.dto.LoginResponseDto
import com.housweet.data.network.dto.toAuthToken
import com.housweet.data.network.dto.toCoordinate
import com.housweet.domain.model.AuthToken
import com.housweet.domain.model.Coordinate
import com.housweet.domain.model.isAccessTokenExpired
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
    private val roomRepository: RoomRepository,
    private val roomLocalDataSource: RoomLocalDataSource
) : AuthRepository {
    override suspend fun loginWithKakao(
        socialId: String,
        accessToken: String,
        email: String
    ): Flow<Result<Int>> = flow {
        try {
            val response = authRemoteDataSource.loginWithKakao(
                socialId = socialId,
                accessToken = accessToken,
                email = email
            )

            val authToken = response.body<LoginResponseDto>().toAuthToken()
            Log.d("TokenCheck", "Housweet accessToken: ${authToken.accessToken}")
            authLocalDataSource.saveAuthToken(authToken)

            // accessToken 만료됐으면 refresh
            if (authToken.isAccessTokenExpired()) {
                val refreshed = authRemoteDataSource.refreshAccessToken(authToken.refreshToken)
                val newToken = AuthToken(refreshed.accessToken, authToken.refreshToken)
                authLocalDataSource.saveAuthToken(newToken)
            }

            //로그인 하자마자 roomId 저장
            val roomId = roomRepository.getMyRoomId()
            roomLocalDataSource.saveRoomId(roomId)

            emit(Result.success(response.status.value))
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

    override suspend fun geoCodingWithNaver(query: String): Flow<Result<Coordinate>> = flow {
        try {
            val response = authRemoteDataSource.geoCodingWithNaver(query)
            val coordinate = response.toCoordinate()
            emit(Result.success(coordinate))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}