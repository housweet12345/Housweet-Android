package com.housweet.domain.usecase.start

import com.housweet.domain.repository.AccessRoomRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AccessRoomWithInviteCodeUseCase @Inject constructor(
    private val accessRoomRepository: AccessRoomRepository
) {
    suspend operator fun invoke(inviteCode: String): Flow<Result<Boolean>> {
        return accessRoomRepository.accessRoomWithInviteCode(inviteCode)
    }
}