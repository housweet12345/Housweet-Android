package com.housweet.data.repository

import com.housweet.data.network.CommunityRemoteDataSource
import com.housweet.data.network.dto.toNearByPostCountDataModel
import com.housweet.data.network.dto.toRoomPostsByLocationDataModel
import com.housweet.domain.model.NearByPostCountDataModel
import com.housweet.domain.model.RoomPostsByLocationDataModel
import com.housweet.domain.repository.CommunityRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CommunityRepositoryImpl @Inject constructor(
    private val communityRemoteDataSource: CommunityRemoteDataSource
) : CommunityRepository {
    override suspend fun getNearbyPostCount(
        latitude: Double,
        longitude: Double,
        filteringDistance: Int
    ): Flow<Result<List<NearByPostCountDataModel>>> = flow {
        try {
            val response = communityRemoteDataSource.getNearbyPostCount(latitude, longitude, filteringDistance)
            emit(Result.success(response.toNearByPostCountDataModel()))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override suspend fun getRoomPostsByLocation(searchWord: String): Flow<Result<List<RoomPostsByLocationDataModel>>> = flow {
        try {
            val response = communityRemoteDataSource.getRoomPostsByLocation(searchWord)
            emit(Result.success(response.toRoomPostsByLocationDataModel()))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override suspend fun clickBookMark(roomPostingId: Int): Flow<Result<Boolean>> = flow {
        try {
            val response = communityRemoteDataSource.clickBookMark(roomPostingId)
            emit(Result.success(response))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override suspend fun unClickBookMark(roomPostingId: Int): Flow<Result<Boolean>> = flow {
        try {
            val response = communityRemoteDataSource.unClickBookMark(roomPostingId)
            emit(Result.success(response))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override suspend fun getRoomPostDetail(roomPostingId: Int) {
        try {
            communityRemoteDataSource.getRoomPostDetail(roomPostingId)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}