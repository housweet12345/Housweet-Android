package com.housweet.domain.usecase.start

import kotlinx.coroutines.flow.Flow

fun interface CreateRoomUseCase {
    suspend operator fun invoke(name: String): Flow<Result<Boolean>>
}