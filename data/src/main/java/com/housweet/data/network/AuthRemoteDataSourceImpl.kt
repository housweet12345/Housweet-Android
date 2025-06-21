package com.housweet.data.network

import com.housweet.data.BuildConfig
import com.housweet.data.network.dto.KakaoLoginRequest
import com.housweet.data.network.dto.RefreshTokenRequest
import com.housweet.data.network.dto.TokenResponseDto
import io.ktor.client.call.body
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
) : AuthRemoteDataSource {
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
}