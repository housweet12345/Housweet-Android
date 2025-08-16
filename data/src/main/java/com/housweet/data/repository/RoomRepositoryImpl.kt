package com.housweet.data.repository

import com.housweet.data.BuildConfig
import com.housweet.data.dto.RoomHomeResponseDto
import com.housweet.data.dto.RoomMemberDto
import com.housweet.data.dto.UpdateMoodRequestDto
import com.housweet.data.network.KtorService
import com.housweet.domain.model.home.RoomHomeModel
import com.housweet.domain.model.home.RoomMemberModel
import com.housweet.domain.repository.RoomRepository
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
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

    override suspend fun getRoomMembers(roomId: Int): Result<List<RoomMemberModel>> {
        return runCatching {
            val response: List<RoomMemberDto> = client.get("${BuildConfig.BASE_URL}/room/rooms/$roomId/members").body()
            response.map { it.mapToRoomMemberModel() }
        }
    }

    override suspend fun updateMood(roomId: Int, feeling: String): Result<RoomMemberModel> {
        return runCatching {
            val requestDto = UpdateMoodRequestDto(feeling = feeling)
            val response: RoomMemberDto = client.patch("${BuildConfig.BASE_URL}/room/room-members/$roomId/") {
                contentType(ContentType.Application.Json)
                setBody(requestDto)
            }.body()
            response.mapToRoomMemberModel()
        }
    }
}