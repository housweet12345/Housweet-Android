package com.housweet.domain.usecase

import com.housweet.domain.repository.AccessRoomRepository
import kotlinx.coroutines.flow.Flow

class AccessRoomWithInviteCodeUseCase(
    private val accessRoomRepository: AccessRoomRepository
) {
    suspend operator fun invoke(inviteCode: String): Flow<Result<Boolean>> {
        return accessRoomRepository.accessRoomWithInviteCode(inviteCode)
    }
}