package com.housweet.domain.repository

import com.housweet.domain.model.HouseRegisterModel
import com.housweet.domain.model.RoomPostingDetail

interface HouseRegisterRepository {
    suspend fun registerHouse(model: HouseRegisterModel)
    suspend fun getPostingDetail(id: Int): RoomPostingDetail
    suspend fun updateHouse(id: Int, model: HouseRegisterModel)
}