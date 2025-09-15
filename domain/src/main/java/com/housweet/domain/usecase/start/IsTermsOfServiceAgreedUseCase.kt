package com.housweet.domain.usecase.start


fun interface IsTermsOfServiceAgreedUseCase {
    suspend operator fun invoke(): Result<Boolean>
}