package com.housweet.domain.usecase.start

import com.housweet.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class IsTermsOfServiceAgreedUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Flow<Result<Boolean>> {
        return authRepository.isTermsOfServiceAgreed()
    }
}