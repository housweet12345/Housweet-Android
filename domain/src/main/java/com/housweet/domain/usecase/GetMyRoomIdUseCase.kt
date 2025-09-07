package com.housweet.domain.usecase

interface GetMyRoomIdUseCase {
    suspend operator fun invoke(): Int
}