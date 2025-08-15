package com.housweet.data.repository

import com.housweet.data.network.NoticeRemoteDataSource
import com.housweet.data.network.dto.toDomain
import com.housweet.domain.model.Notice
import com.housweet.domain.repository.NoticeRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoticeRepositoryImpl @Inject constructor(
    private val remote: NoticeRemoteDataSource
) : NoticeRepository{
    override suspend fun getNotices(): List<Notice> =
        remote.getNotices().map { it.toDomain() }

    override suspend fun getNotice(id: Int): Notice =
        remote.getNotice(id).toDomain()
}