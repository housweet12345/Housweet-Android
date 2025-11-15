package com.housweet.data.manager

import com.housweet.data.BuildConfig
import com.housweet.data.local.DebugConfigLocalDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BaseUrlManager @Inject constructor(
    private val debugConfigLocalDataSource: DebugConfigLocalDataSource
) {
    suspend fun getBaseUrl(): String {
        return if (BuildConfig.IS_DEBUG) {
            debugConfigLocalDataSource.getBaseUrlSync() ?: BuildConfig.BASE_URL
        } else {
            BuildConfig.BASE_URL
        }
    }

    suspend fun getUserBaseUrl(): String {
        return if (BuildConfig.IS_DEBUG) {
            debugConfigLocalDataSource.getUserBaseUrlSync() ?: BuildConfig.USER_BASE_URL
        } else {
            BuildConfig.USER_BASE_URL
        }
    }

    suspend fun setDebugBaseUrl(url: String) {
        if (BuildConfig.IS_DEBUG) {
            debugConfigLocalDataSource.setBaseUrl(url)
        }
    }

    suspend fun setDebugUserBaseUrl(url: String) {
        if (BuildConfig.IS_DEBUG) {
            debugConfigLocalDataSource.setUserBaseUrl(url)
        }
    }

    suspend fun clearDebugUrls() {
        if (BuildConfig.IS_DEBUG) {
            debugConfigLocalDataSource.clearUrls()
        }
    }

    fun isDebugMode(): Boolean = BuildConfig.IS_DEBUG
}