package com.housweet.domain.usecase

data class UseCases (
    val loginWithKakaoUseCase: LoginWithKakaoUseCase,
    val checkLoginUseCase: CheckLoginUseCase,
    val geoCodingWithNaverUseCase: GeoCodingWithNaverUseCase,
    val createRoomUseCase: CreateRoomUseCase,
)