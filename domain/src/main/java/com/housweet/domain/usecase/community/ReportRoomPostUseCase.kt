package com.housweet.domain.usecase.community

import com.housweet.domain.repository.CommunityRepository
import kotlinx.coroutines.flow.Flow

class ReportRoomPostUseCase(
    private val communityRepository: CommunityRepository
) {
    suspend operator fun invoke(roomPostingId: Int): Flow<Result<Boolean>> {
        return communityRepository.reportRoomPost(roomPostingId)
    }
}