package com.housweet.data.network

import com.housweet.data.BuildConfig
import com.housweet.data.model.request.RegisterHouseRequest
import com.housweet.data.network.dto.UpdateHouseRequest
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import javax.inject.Inject

class HouseRegisterRemoteDataSourceImpl @Inject constructor(
    private val client: HttpClient
) : HouseRegisterRemoteDataSource {

    private val BASE_URL = BuildConfig.BASE_URL

    override suspend fun registerHouse(body: RegisterHouseRequest) {
        client.post("${BASE_URL}/room/room-postings/") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
    }

    // 방 공고 상세 조회
    override suspend fun getPostingDetail(id: Int): PostingDetailDto {
        return client.get("$BASE_URL/room/room-postings/$id/").body()
    }

    // 올린 방 수정
    override suspend fun updateHouse(id: Int, body: UpdateHouseRequest) {
        client.patch("$BASE_URL/room/room-postings/$id/") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
    }
}