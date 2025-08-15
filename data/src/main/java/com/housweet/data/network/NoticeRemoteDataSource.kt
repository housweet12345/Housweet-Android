package com.housweet.data.network

import com.housweet.data.network.dto.NoticeDto
import io.ktor.client.statement.HttpResponse

interface NoticeRemoteDataSource {
    suspend fun getNotices(): List<NoticeDto>
    suspend fun getNotice(id: Int): NoticeDto
}