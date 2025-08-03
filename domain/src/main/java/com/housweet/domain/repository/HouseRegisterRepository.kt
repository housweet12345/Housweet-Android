package com.housweet.domain.repository

import com.housweet.domain.model.HouseRegisterModel

interface HouseRegisterRepository {
    suspend fun registerHouse(model: HouseRegisterModel)
}