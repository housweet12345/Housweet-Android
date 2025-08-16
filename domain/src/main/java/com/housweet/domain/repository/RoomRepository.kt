package com.housweet.domain.repository

import com.housweet.domain.model.home.RoomHomeModel

interface RoomRepository {
    suspend fun getRoomHome(): Result<RoomHomeModel>
}