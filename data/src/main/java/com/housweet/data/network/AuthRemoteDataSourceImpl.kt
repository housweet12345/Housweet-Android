package com.housweet.data.network

import android.content.Context
import com.housweet.data.BuildConfig
import com.housweet.data.network.dto.GeoCodingRequest
import com.housweet.data.network.dto.GeoCodingResponseDto
import com.housweet.data.network.dto.KakaoLoginRequest
import com.housweet.data.network.dto.LoginResponseDto
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
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

    override suspend fun geoCodingWithNaver(query: String): GeoCodingResponseDto {
        return httpClient.get("https://maps.apigw.ntruss.com/map-geocode/v2/geocode?query=${query}") {
            headers {
                append("X-NCP-APIGW-API-KEY-ID", BuildConfig.NAVER_CLIENT_ID)
                append("X-NCP-APIGW-API-KEY", BuildConfig.NAVER_CLIENT_SECRET)
            }
            contentType(ContentType.Application.Json)
        }.body()
    }
}