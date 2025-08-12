package com.housweet.domain.repository

import com.housweet.domain.model.NotificationModel

interface NotificationRepository {
    suspend fun getNotifications(): List<NotificationModel>
}