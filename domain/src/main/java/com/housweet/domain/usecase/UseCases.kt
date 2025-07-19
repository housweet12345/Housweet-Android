package com.housweet.domain.usecase

data class UseCases (
    val loginWithKakaoUseCase: LoginWithKakaoUseCase,
    val logoutUseCase: LogoutUseCase,
    val checkLoginUseCase: CheckLoginUseCase,
    val createRoomUseCase: CreateRoomUseCase,
    val accessRoomWithInviteCodeUseCase: AccessRoomWithInviteCodeUseCase,
    val agreeTermsOfServiceUseCase: AgreeTermsOfServiceUseCase,
    val isTermsOfServiceAgreedUseCase: IsTermsOfServiceAgreedUseCase
)