package com.housweet.data.network

import com.housweet.data.local.AuthLocalDataSource
import javax.inject.Inject

//비즈니스 계층, 앱 내부 로직 (토큰 불러오기, 데이터 가공 등)
class RoomRepository @Inject constructor(
    private val remoteDataSource: RoomRemoteDataSource,
    private val authLocalDataSource: AuthLocalDataSource
) {
    suspend fun getMyRoomId(): Int {
        val token = authLocalDataSource.getAuthToken()
            ?: throw IllegalStateException("AccessToken 없음")
        return remoteDataSource.getMyRoomInfo(token.accessToken).id
    }
}