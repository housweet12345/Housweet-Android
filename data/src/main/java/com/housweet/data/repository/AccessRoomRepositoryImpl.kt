package com.housweet.data.repository

import com.housweet.data.network.AccessRoomRemoteDataSource
import com.housweet.domain.repository.AccessRoomRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccessRoomRepositoryImpl @Inject constructor(
    private val accessRoomRemoteDateSource: AccessRoomRemoteDataSource
) : AccessRoomRepository {
    override suspend fun createRoom(name: String): Flow<Result<Boolean>> = flow {
        try {
            val response = accessRoomRemoteDateSource.createRoom(name)
            emit(Result.success(response))
        } catch (ex: Exception) {
            emit(Result.failure(ex))
        }
    }

    override suspend fun accessRoomWithInviteCode(inviteCode: String): Flow<Result<Boolean>> = flow {
            try {
                val response = accessRoomRemoteDateSource.accessRoomWithInviteCode(inviteCode)
                emit(Result.success(response))
            } catch (ex: Exception) {
                emit(Result.failure(ex))
            }
        }
}