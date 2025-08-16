package com.housweet.domain.repository

import com.housweet.domain.model.home.RoomHomeModel
import com.housweet.domain.model.home.RoomMemberModel

interface RoomRepository {
    suspend fun getRoomHome(): Result<RoomHomeModel>
    suspend fun getRoomMembers(roomId: Int): Result<List<RoomMemberModel>>
    suspend fun updateMood(roomId: Int, feeling: String): Result<RoomMemberModel>
}