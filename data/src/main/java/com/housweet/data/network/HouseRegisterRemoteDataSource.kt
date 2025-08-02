package com.housweet.data.network

import com.housweet.data.model.request.RegisterHouseRequest

interface HouseRegisterRemoteDataSource {
    suspend fun registerHouse(body: RegisterHouseRequest)
}