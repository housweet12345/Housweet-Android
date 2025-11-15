package com.housweet.domain.usecase.debug

interface SetDebugConfigUseCase {
    suspend fun setBaseUrl(url: String)
    suspend fun setUserBaseUrl(url: String)
    suspend fun clearDebugUrls()
}