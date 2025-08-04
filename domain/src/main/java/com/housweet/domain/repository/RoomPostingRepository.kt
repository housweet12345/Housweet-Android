package com.housweet.domain.repository

import com.housweet.domain.model.RoomPost

interface RoomPostingRepository {
    suspend fun updatePostVisibility(postingId: Int, isVisible: Boolean)
    suspend fun getRoomPosting(id: Int): RoomPost
}