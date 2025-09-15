package com.housweet.domain.usecase.start


fun interface IsBelongToRoomUseCase {
    suspend operator fun invoke(): Result<Boolean>
}