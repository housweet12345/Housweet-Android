package com.housweet.data.network

import com.housweet.data.response.NoticeResponse

interface NoticeRemoteDataSource {
    suspend fun getNotices(): List<NoticeResponse>
    suspend fun getNotice(id: Int): NoticeResponse
}