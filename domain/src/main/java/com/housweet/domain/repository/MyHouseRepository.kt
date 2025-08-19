package com.housweet.domain.repository

import com.housweet.domain.model.MyHouse

interface MyHouseRepository {
    suspend fun getMyHouseOrNull(): MyHouse?
    suspend fun updateMyHouseName(roomId: Int, name: String): MyHouse
    suspend fun refreshInviteCode(): MyHouse
    suspend fun deleteMyHouse(roomId: Int)
}