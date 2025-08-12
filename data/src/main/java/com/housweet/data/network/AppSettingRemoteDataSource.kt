package com.housweet.data.network

import com.housweet.data.network.dto.AppSettingItemDto
import com.housweet.data.network.dto.AppSettingResponseDto

interface AppSettingRemoteDataSource {
    suspend fun getAppSettings(): AppSettingResponseDto
    suspend fun updateAppSetting(settingId: Int, isEnabled: Boolean): AppSettingItemDto
}