package com.housweet.data.repository

import android.util.Log
import com.housweet.data.datasource.RoomRemoteDataSource
import com.housweet.data.local.AuthLocalDataSource
import javax.inject.Inject

//비즈니스 계층, 앱 내부 로직 (토큰 불러오기, 데이터 가공 등)
class RoomRepository @Inject constructor(
    private val remoteDataSource: RoomRemoteDataSource,
    private val authLocalDataSource: AuthLocalDataSource
) {
    suspend fun getMyRoomId(): Int {
        try {
            val token = authLocalDataSource.getAuthToken()
                ?: throw IllegalStateException("AccessToken 없음")
            return remoteDataSource.getMyRoomInfo(token.accessToken).id
        } catch (e: Exception) {
            if (e.message.toString().contains("Room not found")) return -1
            else throw e
        }
    }
}