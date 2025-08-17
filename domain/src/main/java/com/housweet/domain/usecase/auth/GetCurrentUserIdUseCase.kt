package com.housweet.domain.usecase.auth

import com.housweet.domain.repository.AuthRepository
import javax.inject.Inject

class GetCurrentUserIdUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Int? {
        return authRepository.getCurrentUserId()
    }
}