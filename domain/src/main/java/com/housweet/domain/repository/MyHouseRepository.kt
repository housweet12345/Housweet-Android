package com.housweet.domain.repository

import com.housweet.domain.model.MyHouse

interface MyHouseRepository {
    suspend fun getMyHouse(): MyHouse
    suspend fun updateMyHouseName(roomId: Int, name: String): MyHouse
}