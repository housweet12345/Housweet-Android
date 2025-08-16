package com.housweet.data.repository

import com.housweet.data.BuildConfig
import com.housweet.data.dto.RoomHomeResponseDto
import com.housweet.data.network.KtorService
import com.housweet.domain.model.home.RoomHomeModel
import com.housweet.domain.repository.RoomRepository
import io.ktor.client.call.body
import io.ktor.client.request.get
import javax.inject.Inject

class RoomRepositoryImpl @Inject constructor(
    private val ktorService: KtorService
): RoomRepository {

    private val client by lazy { ktorService.createHttpClient() }

    override suspend fun getRoomHome(): Result<RoomHomeModel> {
        return runCatching {
            val response: RoomHomeResponseDto = client.get("${BuildConfig.BASE_URL}/room/home/").body()
            response.mapToRoomHomeModel()
        }
    }
}