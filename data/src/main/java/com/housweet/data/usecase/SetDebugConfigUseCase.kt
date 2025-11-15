package com.housweet.data.usecase

import com.housweet.data.manager.BaseUrlManager
import com.housweet.data.network.KtorService
import com.housweet.domain.usecase.debug.SetDebugConfigUseCase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SetDebugConfigUseCaseImpl @Inject constructor(
    private val baseUrlManager: BaseUrlManager,
    private val ktorService: KtorService
) : SetDebugConfigUseCase {
    override suspend fun setBaseUrl(url: String) {
        baseUrlManager.setDebugBaseUrl(url)
        ktorService.recreateHttpClient()
    }

    override suspend fun setUserBaseUrl(url: String) {
        baseUrlManager.setDebugUserBaseUrl(url)
        ktorService.recreateHttpClient()
    }

    override suspend fun clearDebugUrls() {
        baseUrlManager.clearDebugUrls()
        ktorService.recreateHttpClient()
    }
}