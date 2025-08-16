package com.housweet.domain.usecase.home

import com.housweet.domain.model.home.RoomMemberModel
import com.housweet.domain.repository.RoomRepository
import javax.inject.Inject

class UpdateMoodUseCase @Inject constructor(
    private val roomRepository: RoomRepository
) {
    suspend operator fun invoke(roomId: Int, feeling: String): Result<RoomMemberModel> {
        return roomRepository.updateMood(roomId, feeling)
    }
}