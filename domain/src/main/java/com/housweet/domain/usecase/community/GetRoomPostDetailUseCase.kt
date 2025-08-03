package com.housweet.domain.usecase.community

import com.housweet.domain.repository.CommunityRepository

class GetRoomPostDetailUseCase(
    private val communityRepository: CommunityRepository
) {
    suspend operator fun invoke(roomPostingId: Int) {
        communityRepository.getRoomPostDetail(roomPostingId)
    }
}