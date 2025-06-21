package com.housweet.domain.repository

import kotlinx.coroutines.flow.Flow

interface AccessRoomRepository {
    suspend fun createRoom(name: String): Flow<Result<Boolean>>
}