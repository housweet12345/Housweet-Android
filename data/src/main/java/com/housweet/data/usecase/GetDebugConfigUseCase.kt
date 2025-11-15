package com.housweet.data.usecase

import com.housweet.data.manager.BaseUrlManager
import com.housweet.domain.usecase.debug.GetDebugConfigUseCase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetDebugConfigUseCaseImpl @Inject constructor(
    private val baseUrlManager: BaseUrlManager
) : GetDebugConfigUseCase {
    override suspend fun getCurrentBaseUrl(): String = baseUrlManager.getBaseUrl()
    override suspend fun getCurrentUserBaseUrl(): String = baseUrlManager.getUserBaseUrl()
    override fun isDebugMode(): Boolean = baseUrlManager.isDebugMode()
}