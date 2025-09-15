package com.housweet.data.repository

import com.housweet.data.datasource.AccessRoomRemoteDataSource
import com.housweet.domain.repository.AccessRoomRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccessRoomRepositoryImpl @Inject constructor(
    private val accessRoomRemoteDateSource: AccessRoomRemoteDataSource
) : AccessRoomRepository {
    override suspend fun createRoom(name: String): Result<Boolean> {
        return try {
            val response = accessRoomRemoteDateSource.createRoom(name)
            Result.success(response)
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }

    override suspend fun accessRoomWithInviteCode(inviteCode: String): Result<Boolean> {
        return try {
            val response = accessRoomRemoteDateSource.accessRoomWithInviteCode(inviteCode)
            Result.success(response)
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }
}