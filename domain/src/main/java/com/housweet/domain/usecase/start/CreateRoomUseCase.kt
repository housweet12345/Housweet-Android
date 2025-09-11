package com.housweet.domain.usecase.start


fun interface CreateRoomUseCase {
    suspend operator fun invoke(name: String): Result<Boolean>
}