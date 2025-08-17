package com.housweet.data.network

import com.housweet.data.model.request.RegisterHouseRequest
import com.housweet.data.network.dto.UpdateHouseRequest

interface HouseRegisterRemoteDataSource {
    suspend fun registerHouse(body: RegisterHouseRequest)
    suspend fun getPostingDetail(id: Int): PostingDetailDto
    suspend fun updateHouse(id: Int, body: UpdateHouseRequest)
}