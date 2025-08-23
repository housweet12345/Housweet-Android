package com.housweet.domain.usecase.community

import com.housweet.domain.model.RoomPostDetailDataModel
import com.housweet.domain.repository.CommunityRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRoomPostDetailUseCase @Inject constructor(
    private val communityRepository: CommunityRepository
) {
    suspend operator fun invoke(roomPostingId: Int): Flow<Result<RoomPostDetailDataModel>> {
        return communityRepository.getRoomPostDetail(roomPostingId)
    }
}