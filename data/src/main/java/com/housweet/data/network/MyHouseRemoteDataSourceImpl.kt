package com.housweet.data.network

import android.util.Log
import com.housweet.data.network.dto.MyHouseDto
import com.housweet.data.network.dto.UpdateMyHouseNameRequest
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import javax.inject.Inject

class MyHouseRemoteDataSourceImpl @Inject constructor(
    private val client: HttpClient
) : MyHouseRemoteDataSource {

    private val base = "http://43.200.10.89"

    override suspend fun getMyHouse(): MyHouseDto {
        return client.get("$base/room/rooms/me").body()
    }

    override suspend fun updateMyHouseName(roomId: Int, name: String): MyHouseDto {
        Log.d("MyHouseRemote", "PATCH $base/room/rooms/$roomId/, name=$name")
        return client.patch("$base/room/rooms/$roomId/") {
            contentType(ContentType.Application.Json)
            setBody(UpdateMyHouseNameRequest(name))
        }.body()
    }

    override suspend fun refreshInviteCode(): MyHouseDto =
        client.post("$base/room/rooms/new_invite_code/") {
            contentType(ContentType.Application.Json)
        }.body()

    // 👇 하우스 삭제 (DELETE /room/rooms/{room_id}/  → 204)
    override suspend fun deleteMyHouse(roomId: Int) {
        val res = client.delete("$base/room/rooms/$roomId/") // ← ← 꼭 슬래시!
        if (!res.status.isSuccess()) {
            throw IllegalStateException("Delete failed: ${res.status.value} ${res.bodyAsText()}")
        }
        // 204면 여기서 그냥 정상 종료
    }
}