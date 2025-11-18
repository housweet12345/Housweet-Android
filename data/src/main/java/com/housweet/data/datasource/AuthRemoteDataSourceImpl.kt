package com.housweet.data.datasource

import com.housweet.data.BuildConfig
import com.housweet.data.constants.ApiEndpoints
import com.housweet.data.network.KtorService
import com.housweet.data.request.AgreeTermsOfServiceRequest
import com.housweet.data.response.IsTermsOfServiceAgreedResponse
import com.housweet.data.request.KakaoLoginRequest
import com.housweet.data.response.ProfileResponse
import com.housweet.data.response.RefreshResponse
import com.housweet.data.request.RefreshTokenRequest
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRemoteDataSourceImpl @Inject constructor(
    private val ktorService: KtorService
): AuthRemoteDataSource {
    private val baseUrl = BuildConfig.BASE_URL

    private val client: HttpClient
        get() = ktorService.getHttpClient()
    private val clientForRefresh by lazy { ktorService.createHttpClientForRefresh() }

    override suspend fun loginWithKakao(
        socialId: String,
        accessToken: String,
        email: String
    ): HttpResponse {
        val response = client.post("$baseUrl/${ApiEndpoints.Auth.LOGIN}") {
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

    override suspend fun refreshAccessToken(refreshToken: String): RefreshResponse {
        return clientForRefresh.post("$baseUrl/${ApiEndpoints.Auth.TOKEN_REFRESH}") {
            contentType(ContentType.Application.Json)
            setBody(RefreshTokenRequest(refreshToken = refreshToken))
        }.body()
    }

    override suspend fun agreeTermsOfService(): Boolean {
        val response = client.patch("$baseUrl/${ApiEndpoints.User.SETTINGS_ME}") {
            contentType(ContentType.Application.Json)
            setBody(
                AgreeTermsOfServiceRequest(
                    termsOfServiceAgreed = true
                )
            )
        }

        return response.status == HttpStatusCode.OK
    }

    override suspend fun isTermsOfServiceAgreed(): IsTermsOfServiceAgreedResponse {
        return client.patch("$baseUrl/${ApiEndpoints.User.SETTINGS_ME}") {
            contentType(ContentType.Application.Json)
        }.body()
    }

    override suspend fun isSetProfile(userId: Int): Boolean {
        val response = client.get("${BuildConfig.USER_BASE_URL}/${ApiEndpoints.User.profileById(userId)}")
        val profile = response.body<ProfileResponse>()
        return profile.yearOfBirth != 0
    }

    override suspend fun isBelongToRoom(): Boolean {
        val response = client.get("$baseUrl/${ApiEndpoints.Room.ROOMS_ME}")
        return response.status == HttpStatusCode.OK
    }

    override suspend fun deleteAccount(): Boolean {
        val response = client.post("$baseUrl/${ApiEndpoints.Auth.WITHDRAW}")
        val isSuccess = response.status in setOf(HttpStatusCode.OK, HttpStatusCode.NoContent)
        recreateHttpClient()

        return isSuccess
    }

    override fun recreateHttpClient() {
        ktorService.recreateHttpClient()
    }
}