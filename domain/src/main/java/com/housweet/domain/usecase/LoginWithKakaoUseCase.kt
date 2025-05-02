package com.housweet.domain.usecase

import com.housweet.domain.model.AuthToken
import com.housweet.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

class LoginWithKakaoUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(kakaoToken: String): Flow<Result<AuthToken>> {
        return authRepository.loginWithKakao(kakaoToken)
    }
}