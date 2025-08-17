package com.housweet.data.network

import com.housweet.data.BuildConfig
import com.housweet.data.network.dto.AgreeTermsOfServiceRequest
import com.housweet.data.network.dto.IsTermsOfServiceAgreedResponseDto
import com.housweet.data.network.dto.KakaoLoginRequest
import com.housweet.data.network.dto.RefreshResponseDto
import com.housweet.data.network.dto.RefreshTokenRequest
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.patch
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

    override suspend fun refreshAccessToken(refreshToken: String): RefreshResponseDto {
        return httpClientForRefresh.post("$BASE_URL/auth/token/refresh") {
            contentType(ContentType.Application.Json)
            setBody(RefreshTokenRequest(refreshToken = refreshToken))
        }.body()
    }

    override suspend fun agreeTermsOfService(): Boolean {
        val response = httpClient.patch("$BASE_URL/user/settings/me/") {
            contentType(ContentType.Application.Json)
            setBody(
                AgreeTermsOfServiceRequest(
                    termsOfServiceAgreed = true
                )
            )
        }

        return response.status.value == 200
    }

    override suspend fun isTermsOfServiceAgreed(): IsTermsOfServiceAgreedResponseDto {
        return httpClient.patch("$BASE_URL/user/settings/me/") {
            contentType(ContentType.Application.Json)
        }.body()
    }

    override suspend fun isSetProfile(userId: Int): Boolean {
        val response = httpClient.get("${BuildConfig.USER_BASE_URL}/profile/$userId")
        return response.status.value == 200
    }

    override suspend fun isBelongToRoom(): Boolean {
        val response = httpClient.get("$BASE_URL/room/rooms/me/")
        return response.status.value == 200
    }
}