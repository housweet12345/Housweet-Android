package com.housweet.domain.local

interface RoomLocalDataSource {
    suspend fun saveRoomId(id: Int)
    suspend fun getRoomId(): Int?
    suspend fun clearRoomId()
}