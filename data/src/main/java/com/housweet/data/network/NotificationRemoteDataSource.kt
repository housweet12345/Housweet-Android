package com.housweet.data.network

import com.housweet.data.network.dto.NotificationDto

interface NotificationRemoteDataSource {
    suspend fun getNotifications(): List<NotificationDto>
}