package com.housweet.data.network

import com.housweet.data.response.NotificationResponse

interface NotificationRemoteDataSource {
    suspend fun getNotifications(): List<NotificationResponse>
}