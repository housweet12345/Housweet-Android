package com.housweet.data.datasource

import com.housweet.data.BuildConfig
import com.housweet.data.constants.ApiEndpoints
import com.housweet.data.network.KtorService
import com.housweet.data.response.NotificationResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationRemoteDataSourceImpl @Inject constructor(
    private val ktorService: KtorService
) : NotificationRemoteDataSource {
    private val client: HttpClient
        get() = ktorService.getHttpClient()
    private val baseUrl = BuildConfig.BASE_URL

    override suspend fun getNotifications(): List<NotificationResponse> {
        return client.get("$baseUrl/${ApiEndpoints.Communications.NOTIFICATIONS}").body()
    }
}