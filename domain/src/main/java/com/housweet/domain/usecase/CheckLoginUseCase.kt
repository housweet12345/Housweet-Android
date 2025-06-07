package com.housweet.domain.usecase

import com.housweet.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

class CheckLoginUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Flow<Result<Boolean>> {
        return authRepository.checkLogin()
    }
}