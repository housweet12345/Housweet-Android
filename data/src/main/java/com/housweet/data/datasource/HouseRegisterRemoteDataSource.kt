package com.housweet.data.datasource

import com.housweet.data.request.RegisterHouseRequest
import com.housweet.data.request.UpdateHouseRequest
import com.housweet.data.response.PostingDetailResponse

interface HouseRegisterRemoteDataSource {
    suspend fun registerHouse(body: RegisterHouseRequest)
    suspend fun getPostingDetail(id: Int): PostingDetailResponse
    suspend fun updateHouse(id: Int, body: UpdateHouseRequest)
}