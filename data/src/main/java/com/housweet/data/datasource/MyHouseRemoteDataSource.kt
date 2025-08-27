package com.housweet.data.datasource

import com.housweet.data.response.MyHouseResponse

interface MyHouseRemoteDataSource {
    suspend fun getMyHouse(): MyHouseResponse?
    suspend fun updateMyHouseName(roomId: Int, name: String): MyHouseResponse
    suspend fun refreshInviteCode(): MyHouseResponse
    suspend fun deleteMyHouse(roomId: Int)

}