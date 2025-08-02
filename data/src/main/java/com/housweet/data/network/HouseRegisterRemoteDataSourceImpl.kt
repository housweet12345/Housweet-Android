package com.housweet.data.network

import com.housweet.data.model.request.RegisterHouseRequest
import io.ktor.client.HttpClient
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import javax.inject.Inject

class HouseRegisterRemoteDataSourceImpl @Inject constructor(
    private val client: HttpClient
) : HouseRegisterRemoteDataSource {

    override suspend fun registerHouse(body: RegisterHouseRequest) {
        client.post("http://43.200.10.89/room/room-postings/") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
    }
}