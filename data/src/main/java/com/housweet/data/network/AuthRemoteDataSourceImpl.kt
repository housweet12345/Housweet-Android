package com.housweet.data.network

import com.housweet.data.BuildConfig
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
) : AuthRemoteDataSource {
    companion object {
        private const val BASE_URL = BuildConfig.BASE_URL
    }

    private val httpClient by lazy { ktorClient.createHttpClient() }

    override suspend fun loginWithKakao(
        socialId: String,
        accessToken: String,
        email: String
    ): LoginResponseDto {
        return httpClient.post("$BASE_URL/auth/login") {
            contentType(ContentType.Application.Json)
            setBody(
                KakaoLoginRequest(
                    socialId = socialId,
                    accessToken = accessToken,
                    email = email
                )
            )
        }.body()
    }
}