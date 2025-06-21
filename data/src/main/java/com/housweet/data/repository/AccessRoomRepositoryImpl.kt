package com.housweet.data.repository

import android.util.Log
import com.housweet.data.network.AccessRoomRemoteDateSource
import com.housweet.domain.repository.AccessRoomRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccessRoomRepositoryImpl @Inject constructor(
    private val accessRoomRemoteDateSource: AccessRoomRemoteDateSource
) : AccessRoomRepository {
    override suspend fun createRoom(name: String): Flow<Result<Boolean>> = flow {
        try {
            val response = accessRoomRemoteDateSource.createRoom(name)
            Log.d("response", response.toString())
            emit(Result.success(response))
        } catch (ex: Exception) {
            Log.d("response", ex.toString())
            emit(Result.failure(ex))
        }
    }
}