package com.housweet.data.network

import com.housweet.data.BuildConfig
import com.housweet.data.network.dto.NoticeDto
import io.ktor.client.call.body
import io.ktor.client.request.get
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoticeRemoteDataSourceImpl @Inject constructor(
    private val ktorService: KtorService
) : NoticeRemoteDataSource {
    private val client by lazy { ktorService.createHttpClient() }
    private val base = BuildConfig.BASE_URL

    override suspend fun getNotices(): List<NoticeDto> {
        return client.get("$base/communications/notices/").body()
    }

    override suspend fun getNotice(id: Int): NoticeDto {
        return client.get("$base/communications/notices/$id/").body()
    }
}