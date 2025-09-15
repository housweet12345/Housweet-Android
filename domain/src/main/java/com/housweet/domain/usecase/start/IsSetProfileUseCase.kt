package com.housweet.domain.usecase.start


fun interface IsSetProfileUseCase {
    suspend operator fun invoke(): Result<Boolean>
}