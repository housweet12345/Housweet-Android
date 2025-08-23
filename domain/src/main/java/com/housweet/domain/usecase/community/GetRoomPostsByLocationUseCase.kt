package com.housweet.domain.usecase.community

import com.housweet.domain.model.RoomPostsByLocationDataModel
import com.housweet.domain.repository.CommunityRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRoomPostsByLocationUseCase @Inject constructor(
    private val communityRepository: CommunityRepository
) {
    suspend operator fun invoke(searchWord: String): Flow<Result<List<RoomPostsByLocationDataModel>>> {
        return communityRepository.getRoomPostsByLocation(searchWord)
    }
}