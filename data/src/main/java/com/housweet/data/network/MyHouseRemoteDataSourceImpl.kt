package com.housweet.data.network

import android.util.Log
import com.housweet.data.network.dto.MyHouseDto
import com.housweet.data.network.dto.UpdateMyHouseNameRequest
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
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
}