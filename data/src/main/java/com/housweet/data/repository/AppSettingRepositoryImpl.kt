package com.housweet.data.repository

import com.housweet.data.network.AppSettingRemoteDataSource
import com.housweet.data.network.dto.toDomain
import com.housweet.domain.model.AppSettingCategory
import com.housweet.domain.model.AppSettingItem
import com.housweet.domain.repository.AppSettingRepository
import javax.inject.Inject

class AppSettingRepositoryImpl @Inject constructor(
    private val remote: AppSettingRemoteDataSource
) : AppSettingRepository {

    override suspend fun getAppSettings(): List<AppSettingCategory> {
        return remote.getAppSettings().toDomain()
    }

    override suspend fun updateAppSetting(id: Int, isEnabled: Boolean): AppSettingItem {
        return remote.updateAppSetting(id, isEnabled).toDomain()
    }
}