package com.housweet.domain.usecase.start

import com.housweet.domain.repository.AuthRepository
import javax.inject.Inject

class LoginWithKakaoUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        socialId: String,
        accessToken: String,
        email: String
    ): Result<Boolean> {
        return authRepository.loginWithKakao(
            socialId = socialId,
            accessToken = accessToken,
            email = email
        )
    }
}