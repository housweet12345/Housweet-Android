package com.housweet.data.datasource

interface AccessRoomRemoteDataSource {
    suspend fun createRoom(name: String): Boolean
    suspend fun accessRoomWithInviteCode(inviteCode: String): Boolean
}