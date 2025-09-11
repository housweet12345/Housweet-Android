package com.housweet.domain.usecase.start

fun interface AgreeTermsOfServiceUseCase {
    suspend operator fun invoke(): Result<Boolean>
}