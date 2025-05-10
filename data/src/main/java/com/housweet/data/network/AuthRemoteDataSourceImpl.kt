package com.housweet.data.network

import com.housweet.data.network.dto.KakaoLoginRequest
import com.housweet.data.network.dto.LoginResponseDto
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRemoteDataSourceImpl @Inject constructor(
    private val ktorClient: KtorService
): AuthRemoteDataSource {
    companion object {
        private const val BASE_URL = "https://run.mocky.io"
    }

    private val httpClient by lazy { ktorClient.createHttpClient() }

    override suspend fun loginWithKakao(kakaoToken: String): LoginResponseDto {
        return httpClient.post("$BASE_URL/auth/kakao") {
            contentType(ContentType.Application.Json)
            setBody(KakaoLoginRequest(kakaoToken))
        }.body()
    }

    override suspend fun test(): LoginResponseDto {
        return httpClient.get("$BASE_URL/v3/dd91514f-31be-4ff8-8d1c-b298811a7475") {
            contentType(ContentType.Application.Json)
        }.body()
    }
}