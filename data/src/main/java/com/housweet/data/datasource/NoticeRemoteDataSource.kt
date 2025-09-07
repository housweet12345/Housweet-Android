package com.housweet.data.datasource

import com.housweet.data.response.NoticeResponse

interface NoticeRemoteDataSource {
    suspend fun getNotices(): List<NoticeResponse>
    suspend fun getNotice(id: Int): NoticeResponse
}