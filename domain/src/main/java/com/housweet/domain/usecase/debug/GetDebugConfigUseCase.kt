package com.housweet.domain.usecase.debug

interface GetDebugConfigUseCase {
    suspend fun getCurrentBaseUrl(): String
    suspend fun getCurrentUserBaseUrl(): String
    fun isDebugMode(): Boolean
}