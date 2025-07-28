package com.housweet.data.network

interface AccessRoomRemoteDataSource {
    suspend fun createRoom(name: String): Boolean
    suspend fun accessRoomWithInviteCode(inviteCode: String): Boolean
}