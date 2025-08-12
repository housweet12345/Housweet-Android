package com.housweet.data.network

import com.housweet.data.network.dto.BookmarkedPostingListResponse
import com.housweet.data.network.dto.GetNearbyPostCountResponseListDto
import com.housweet.data.network.dto.GetRoomPostDetailResponseDto
import com.housweet.data.network.dto.GetRoomPostsByLocationResponseListDto

interface CommunityRemoteDataSource {
    suspend fun getNearbyPostCount(
        latitude: Double,
        longitude: Double,
        filteringDistance: Int
    ): GetNearbyPostCountResponseListDto
    suspend fun getRoomPostsByLocation(searchWord: String): GetRoomPostsByLocationResponseListDto
    suspend fun getBookmarkedPostings(): BookmarkedPostingListResponse
    suspend fun clickBookMark(roomPostingId: Int): Boolean
    suspend fun unClickBookMark(roomPostingId: Int): Boolean
    suspend fun getRoomPostDetail(roomPostingId: Int): GetRoomPostDetailResponseDto
}