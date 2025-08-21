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
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import javax.inject.Inject

class RoomRepositoryImpl @Inject constructor(
    private val ktorService: KtorService
): RoomRepository {
    private val client by lazy { ktorService.createHttpClient() }
    private val base = BuildConfig.BASE_URL

    private suspend inline fun <reified T> HttpResponse.parseOrFail(op: String): Result<T> {
        return if (status.isSuccess()) {
            runCatching { body<T>() }.mapFailureWithRaw(this, op)
        } else {
            Result.failure(IllegalStateException("$op failed: $status ${runCatching { bodyAsText() }.getOrNull()}"))
        }
    }

    private suspend inline fun <T> Result<T>.mapFailureWithRaw(
        res: HttpResponse,
        op: String
    ): Result<T> = this.fold(
        onSuccess = { Result.success(it) },
        onFailure = { ex ->
            val raw = runCatching { res.bodyAsText() }.getOrNull()
            Result.failure(IllegalStateException("$op parse error: ${ex.message}\nraw=$raw", ex))
        }
    )

    override suspend fun getRoomHome(): Result<RoomHomeModel> {
        val res: HttpResponse = client.get("$base/room/home/")   // 슬래시 O
        return res.parseOrFail<RoomHomeResponseDto>("getRoomHome")
            .map { it.mapToRoomHomeModel() }
    }

    override suspend fun getRoomMembers(roomId: Int): Result<List<RoomMemberModel>> {
        val res: HttpResponse = client.get("$base/room/rooms/$roomId/members/") // ✅ 슬래시 추가
        return res.parseOrFail<List<RoomMemberDto>>("getRoomMembers($roomId)")
            .map { list -> list.map { it.mapToRoomMemberModel() } }
    }

    override suspend fun updateMood(memberId: Int, feeling: String): Result<RoomMemberModel> {
        val req = UpdateMoodRequestDto(feeling = feeling)
        val res: HttpResponse = client.patch("$base/room/room-members/$memberId/") { // 슬래시 O
            contentType(ContentType.Application.Json)
            setBody(req)
        }
        return res.parseOrFail<RoomMemberDto>("updateMood($memberId)")
            .map { it.mapToRoomMemberModel() }
    }
}