package com.housweet.domain.usecase.start

import com.housweet.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

class IsTermsOfServiceAgreedUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Flow<Result<Boolean>> {
        return authRepository.isTermsOfServiceAgreed()
    }
}