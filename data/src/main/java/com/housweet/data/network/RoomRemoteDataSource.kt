package com.housweet.data.network

import com.housweet.data.response.MyRoomResponse

// 데이터 소스 인터페이스, 외부 호출 방식 정의
interface RoomRemoteDataSource {
    suspend fun getMyRoomInfo(accessToken: String): MyRoomResponse
}