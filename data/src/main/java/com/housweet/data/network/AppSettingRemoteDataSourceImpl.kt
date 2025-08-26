package com.housweet.data.network

import android.util.Log
import com.housweet.data.BuildConfig
import com.housweet.data.request.AppSettingUpdateRequest
import com.housweet.data.response.AppSettingResponse
import com.housweet.data.response.AppSettingItemResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.setBody
import javax.inject.Inject

class AppSettingRemoteDataSourceImpl @Inject constructor(
    private val ktorService: KtorService
) : AppSettingRemoteDataSource {

    private val client: HttpClient
        get() = ktorService.getHttpClient()
    private val baseUrl = BuildConfig.BASE_URL

    override suspend fun getAppSettings(): AppSettingResponse {
        return client.get("$baseUrl/user/app-settings/").body()
    }

    override suspend fun updateAppSetting(settingId: Int, isEnabled: Boolean): AppSettingItemResponse {
        Log.d("AppSettingRemote", "PATCH 요청 시작 → settingId: $settingId, isEnabled: $isEnabled")

        val requestBody = AppSettingUpdateRequest(is_enabled = isEnabled)
        Log.d("AppSettingRemote", "요청 바디: $requestBody")

        return client.patch("$baseUrl/user/app-settings/$settingId/") {
            setBody(requestBody)
        }.body()
    }
}