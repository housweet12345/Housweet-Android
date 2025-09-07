package com.housweet.domain.usecase.start

import kotlinx.coroutines.flow.Flow

fun interface AgreeTermsOfServiceUseCase {
    suspend operator fun invoke(): Flow<Result<Boolean>>
}