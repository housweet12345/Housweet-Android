package com.housweet.data.datasource

import com.housweet.data.response.BookmarkedPostingListResponse
import com.housweet.data.response.GetNearbyPostCountResponseListDto
import com.housweet.data.response.GetRoomPostDetailResponse
import com.housweet.data.response.GetRoomPostsByLocationResponseList

interface CommunityRemoteDataSource {
    suspend fun getNearbyPostCount(
        latitude: Double,
        longitude: Double,
        filteringDistance: Int
    ): GetNearbyPostCountResponseListDto
    suspend fun getRoomPostsByLocation(searchWord: String): GetRoomPostsByLocationResponseList
    suspend fun getBookmarkedPostings(): BookmarkedPostingListResponse
    suspend fun clickBookMark(roomPostingId: Int): Boolean
    suspend fun unClickBookMark(roomPostingId: Int): Boolean
    suspend fun getRoomPostDetail(roomPostingId: Int): GetRoomPostDetailResponse
    suspend fun reportRoomPost(roomPostingId: Int): Boolean
}