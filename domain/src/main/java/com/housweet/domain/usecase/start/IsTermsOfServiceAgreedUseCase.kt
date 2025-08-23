package com.housweet.domain.usecase.start

import kotlinx.coroutines.flow.Flow

fun interface IsTermsOfServiceAgreedUseCase {
    suspend operator fun invoke(): Flow<Result<Boolean>>
}