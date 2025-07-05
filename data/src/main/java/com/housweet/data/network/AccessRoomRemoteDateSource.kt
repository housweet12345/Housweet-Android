package com.housweet.data.network

interface AccessRoomRemoteDateSource {
    suspend fun createRoom(name: String): Boolean
}