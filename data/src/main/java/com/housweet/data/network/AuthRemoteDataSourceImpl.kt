package com.housweet.data.network

import com.housweet.data.BuildConfig
import com.housweet.data.request.AgreeTermsOfServiceRequest
import com.housweet.data.network.dto.IsTermsOfServiceAgreedResponseDto
import com.housweet.data.request.KakaoLoginRequest
import com.housweet.data.network.dto.RefreshResponseDto
import com.housweet.data.request.RefreshTokenRequest
import io.ktor.client.HttpClient
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
    private val ktorService: KtorService
): AuthRemoteDataSource {
    companion object {
        private const val BASE_URL = BuildConfig.BASE_URL
    }

    private val httpClient: HttpClient
        get() = ktorService.getHttpClient()
    private val httpClientForRefresh by lazy { ktorService.createHttpClientForRefresh() }

    override suspend fun loginWithKakao(
        socialId: String,
        accessToken: String,
        email: String
    ): HttpResponse {
        val response = httpClient.post("$BASE_URL/auth/login") {
            contentType(ContentType.Application.Json)
            setBody(
                KakaoLoginRequest(
                    socialId = socialId,
                    accessToken = accessToken,
                    email = email
                )
            )
        }

        recreateHttpClient()

        return response
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
        val response = httpClient.get("${BuildConfig.USER_BASE_URL}/profile/$userId/$userId")
        return !response.body<String>().contains("\"nickname\": \"\", \"introduce\": null")
    }

    override suspend fun isBelongToRoom(): Boolean {
        val response = httpClient.get("$BASE_URL/room/rooms/me/")
        return response.status.value == 200
    }

    override suspend fun deleteAccount(): Boolean {
        val response = httpClient.post("$BASE_URL/auth/withdraw")
        val isSuccess = response.status.value == 200 || response.status.value == 204
        recreateHttpClient()

        return isSuccess
    }

    override fun recreateHttpClient() {
        ktorService.recreateHttpClient()
    }
}