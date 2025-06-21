package com.housweet.data.network

import android.content.Context
import com.housweet.data.BuildConfig
import com.housweet.data.network.dto.GeoCodingRequest
import com.housweet.data.network.dto.GeoCodingResponseDto
import com.housweet.data.network.dto.KakaoLoginRequest
import com.housweet.data.network.dto.RefreshTokenRequest
import com.housweet.data.network.dto.TokenResponseDto
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRemoteDataSourceImpl @Inject constructor(
    private val ktorClient: KtorService
): AuthRemoteDataSource {
    companion object {
        private const val BASE_URL = BuildConfig.BASE_URL
    }

    private val httpClient by lazy { ktorClient.createHttpClient() }
    private val httpClientForRefresh by lazy { ktorClient.createHttpClientForRefresh() }

    override suspend fun loginWithKakao(
        socialId: String,
        accessToken: String,
        email: String
    ): HttpResponse {
        return httpClient.post("$BASE_URL/auth/login") {
            contentType(ContentType.Application.Json)
            setBody(
                KakaoLoginRequest(
                    socialId = socialId,
                    accessToken = accessToken,
                    email = email
                )
            )
        }
    }

    override suspend fun refreshAccessToken(refreshToken: String): TokenResponseDto {
        return httpClientForRefresh.post("$BASE_URL/auth/token/refresh") {
            contentType(ContentType.Application.Json)
            setBody(
                RefreshTokenRequest(
                    refreshToken = refreshToken
                )
            )
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