package com.housweet.domain.repository

import com.housweet.domain.model.BookmarkItem
import com.housweet.domain.model.NearByPostCountModel
import com.housweet.domain.model.RoomPostsByLocationDataModel
import kotlinx.coroutines.flow.Flow

interface CommunityRepository {
    suspend fun getNearbyPostCount(
        latitude: Double,
        longitude: Double,
        filteringDistance: Int
    ): Flow<Result<List<NearByPostCountModel>>>
    suspend fun getRoomPostsByLocation(searchWord: String): Flow<Result<List<RoomPostsByLocationDataModel>>>
    suspend fun getBookmarkedPostings(): Result<List<BookmarkItem>>
    suspend fun clickBookMark(roomPostingId: Int): Flow<Result<Boolean>>
    suspend fun unClickBookMark(roomPostingId: Int): Flow<Result<Boolean>>
    suspend fun getRoomPostDetail(roomPostingId: Int)
}