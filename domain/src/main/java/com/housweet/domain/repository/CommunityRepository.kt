package com.housweet.domain.repository

import com.housweet.domain.model.community.NearByPostCountDataModel
import com.housweet.domain.model.community.RoomPostDetailDataModel
import com.housweet.domain.model.BookmarkItem
import com.housweet.domain.model.community.RoomPostsByLocationDataModel
import kotlinx.coroutines.flow.Flow

interface CommunityRepository {
    suspend fun getNearbyPostCount(
        latitude: Double,
        longitude: Double,
        filteringDistance: Int
    ): Flow<Result<List<NearByPostCountDataModel>>>
    suspend fun getRoomPostsByLocation(searchWord: String): Flow<Result<List<RoomPostsByLocationDataModel>>>
    suspend fun getBookmarkedPostings(): Result<List<BookmarkItem>>
    suspend fun clickBookMark(roomPostingId: Int): Flow<Result<Boolean>>
    suspend fun unClickBookMark(roomPostingId: Int): Flow<Result<Boolean>>
    suspend fun getRoomPostDetail(roomPostingId: Int): Flow<Result<RoomPostDetailDataModel>>
    suspend fun reportRoomPost(roomPostingId: Int): Flow<Result<Boolean>>
}