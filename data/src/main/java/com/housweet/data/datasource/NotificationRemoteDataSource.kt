package com.housweet.data.datasource

import com.housweet.data.response.NotificationResponse

interface NotificationRemoteDataSource {
    suspend fun getNotifications(): List<NotificationResponse>
}