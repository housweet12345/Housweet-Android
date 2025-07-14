package com.housweet.domain.usecase

import com.housweet.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

class LoginWithKakaoUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        socialId: String,
        accessToken: String,
        email: String
    ): Flow<Result<Boolean>> {
        return authRepository.loginWithKakao(
            socialId = socialId,
            accessToken = accessToken,
            email = email
        )
    }
}