package com.housweet.data.network

import com.housweet.data.request.RegisterHouseRequest
import com.housweet.data.request.UpdateHouseRequest

interface HouseRegisterRemoteDataSource {
    suspend fun registerHouse(body: RegisterHouseRequest)
    suspend fun getPostingDetail(id: Int): PostingDetailDto
    suspend fun updateHouse(id: Int, body: UpdateHouseRequest)
}