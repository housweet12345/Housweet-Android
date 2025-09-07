package com.housweet.data.datasource

import com.housweet.data.response.AppSettingItemResponse
import com.housweet.data.response.AppSettingResponse

interface AppSettingRemoteDataSource {
    suspend fun getAppSettings(): AppSettingResponse
    suspend fun updateAppSetting(settingId: Int, isEnabled: Boolean): AppSettingItemResponse
}