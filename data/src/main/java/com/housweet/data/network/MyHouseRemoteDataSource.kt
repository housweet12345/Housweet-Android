package com.housweet.data.network

import com.housweet.data.network.dto.MyHouseDto

interface MyHouseRemoteDataSource {
    suspend fun getMyHouse(): MyHouseDto
    suspend fun updateMyHouseName(roomId: Int, name: String): MyHouseDto
}