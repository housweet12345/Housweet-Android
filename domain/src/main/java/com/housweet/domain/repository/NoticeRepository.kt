package com.housweet.domain.repository

import com.housweet.domain.model.Notice

interface NoticeRepository {
    suspend fun getNotices(): List<Notice>
    suspend fun getNotice(id: Int): Notice
}