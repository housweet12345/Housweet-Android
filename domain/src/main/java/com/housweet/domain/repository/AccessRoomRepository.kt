package com.housweet.domain.repository

interface AccessRoomRepository {
    suspend fun createRoom(name: String): Result<Boolean>
    suspend fun accessRoomWithInviteCode(inviteCode: String): Result<Boolean>
}