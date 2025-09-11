package com.housweet.data.repository

import com.housweet.data.datasource.CommunityRemoteDataSource
import com.housweet.data.response.toRoomPostDetailDataModel
import com.housweet.data.response.toNearByPostCountDataModel
import com.housweet.data.response.toDomain
import com.housweet.data.response.toRoomPostsByLocationDataModel
import com.housweet.domain.model.community.RoomPostDetailDataModel
import com.housweet.domain.model.community.NearByPostCountDataModel
import com.housweet.domain.model.BookmarkItem
import com.housweet.domain.model.community.RoomPostsByLocationDataModel
import com.housweet.domain.repository.CommunityRepository
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
    ): Result<List<NearByPostCountDataModel>> {
        return try {
            val response = communityRemoteDataSource.getNearbyPostCount(latitude, longitude, filteringDistance)
            Result.success(response.toNearByPostCountDataModel())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getRoomPostsByLocation(searchWord: String): Result<List<RoomPostsByLocationDataModel>> {
        return try {
            val response = communityRemoteDataSource.getRoomPostsByLocation(searchWord)
            Result.success(response.toRoomPostsByLocationDataModel())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getBookmarkedPostings(): Result<List<BookmarkItem>> = runCatching {
        val res = communityRemoteDataSource.getBookmarkedPostings()
        res.data.map { it.toDomain() }
    }

    override suspend fun clickBookMark(roomPostingId: Int): Result<Boolean> {
        return try {
            val response = communityRemoteDataSource.clickBookMark(roomPostingId)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun unClickBookMark(roomPostingId: Int): Result<Boolean> {
        return try {
            val response = communityRemoteDataSource.unClickBookMark(roomPostingId)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getRoomPostDetail(roomPostingId: Int): Result<RoomPostDetailDataModel> {
        return try {
            val response = communityRemoteDataSource.getRoomPostDetail(roomPostingId)
            Result.success(response.toRoomPostDetailDataModel())
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override suspend fun reportRoomPost(roomPostingId: Int): Result<Boolean> {
        return try {
            val response = communityRemoteDataSource.reportRoomPost(roomPostingId)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}