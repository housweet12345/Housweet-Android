package com.housweet.data.datasource

import android.util.Log
import com.housweet.data.BuildConfig
import com.housweet.data.network.KtorService
import com.housweet.data.response.MyHouseResponse
import com.housweet.data.response.UpdateMyHouseNameRequest
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MyHouseRemoteDataSourceImpl @Inject constructor(
    private val ktorService: KtorService
) : MyHouseRemoteDataSource {
    private val client: HttpClient
        get() = ktorService.getHttpClient()

    private val baseUrl = BuildConfig.MY_HOUSE_BASE_URL

    override suspend fun getMyHouse(): MyHouseResponse? {
        val res: HttpResponse = client.get("$baseUrl/room/rooms/me")
        return when (res.status) {
            HttpStatusCode.OK -> res.body<MyHouseResponse>()
            HttpStatusCode.NotFound -> null                        // 빈 상태
            else -> throw ResponseException(res, "getMyHouse failed: ${res.status} ${res.bodyAsText()}")
        }
    }

    override suspend fun updateMyHouseName(roomId: Int, name: String): MyHouseResponse {
        Log.d("MyHouseRemote", "PATCH $baseUrl/room/rooms/$roomId/, name=$name")
        val res = client.patch("$baseUrl/room/rooms/$roomId/") {
            contentType(ContentType.Application.Json)
            setBody(UpdateMyHouseNameRequest(name))
        }
        if (res.status.isSuccess()) return res.body()
        val text = runCatching { res.bodyAsText() }.getOrDefault("")
        throw ResponseException(res, "updateMyHouseName failed: ${res.status} $text")
    }

    override suspend fun refreshInviteCode(): MyHouseResponse {
        val res = client.post("$baseUrl/room/rooms/new_invite_code/") {
            contentType(ContentType.Application.Json)
        }
        if (res.status.isSuccess()) return res.body()
        val text = runCatching { res.bodyAsText() }.getOrDefault("")
        throw ResponseException(res, "refreshInviteCode failed: ${res.status} $text")
    }

    // 👇 하우스 삭제 (DELETE /room/rooms/{room_id}/  → 204)
    override suspend fun deleteMyHouse(roomId: Int) {
        val res = client.delete("$baseUrl/room/rooms/$roomId/") // ← ← 꼭 슬래시!
        if (!res.status.isSuccess()) {
            throw IllegalStateException("Delete failed: ${res.status.value} ${res.bodyAsText()}")
        }
        // 204면 여기서 그냥 정상 종료
    }
}