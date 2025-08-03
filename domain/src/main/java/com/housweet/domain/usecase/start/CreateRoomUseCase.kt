package com.housweet.domain.usecase.start

import com.housweet.domain.repository.AccessRoomRepository
import kotlinx.coroutines.flow.Flow


class CreateRoomUseCase(
    private val accessRoomRepository: AccessRoomRepository
) {
    suspend operator fun invoke(name: String): Flow<Result<Boolean>> {
        return accessRoomRepository.createRoom(name)
    }
}