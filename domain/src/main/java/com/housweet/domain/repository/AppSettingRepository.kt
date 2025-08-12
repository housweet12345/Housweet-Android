package com.housweet.domain.repository

import com.housweet.domain.model.AppSettingCategory
import com.housweet.domain.model.AppSettingItem

interface AppSettingRepository {
    suspend fun getAppSettings(): List<AppSettingCategory>
    suspend fun updateAppSetting(id: Int, isEnabled: Boolean): AppSettingItem
}