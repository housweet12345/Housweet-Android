package com.housweet.domain.usecase

import com.housweet.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

class LogoutUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke() {
        return authRepository.logout()
    }
}