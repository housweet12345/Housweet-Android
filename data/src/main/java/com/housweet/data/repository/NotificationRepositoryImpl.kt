package com.housweet.data.repository

import com.housweet.data.network.NotificationRemoteDataSource
import com.housweet.data.network.dto.toDomain
import com.housweet.domain.model.NotificationModel
import com.housweet.domain.repository.NotificationRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationRepositoryImpl @Inject constructor(
    private val remoteDataSource: NotificationRemoteDataSource
) : NotificationRepository {
    override suspend fun getNotifications(): List<NotificationModel> {
        return remoteDataSource.getNotifications()
            .map { it.toDomain() }
            .sortedByDescending { it.createdAt } // 최신순 정렬
    }
}