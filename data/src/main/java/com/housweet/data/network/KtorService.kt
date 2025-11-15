package com.housweet.data.network

import android.util.Log
import com.housweet.data.BuildConfig
import com.housweet.data.local.AuthLocalDataSource
import com.housweet.data.manager.BaseUrlManager
import com.housweet.data.request.RefreshTokenRequest
import com.housweet.data.response.RefreshResponse
import com.housweet.domain.event.AuthEvent
import com.housweet.domain.event.AuthEventBus
import com.housweet.domain.model.start.AuthToken
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.accept
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import okhttp3.ConnectionPool
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class KtorService @Inject constructor(
    private val authLocalDataSource: AuthLocalDataSource,
    private val authEventBus: AuthEventBus,
    private val baseUrlManager: BaseUrlManager
) {
    companion object {
        private const val BASE_URL = BuildConfig.BASE_URL
        private const val TAG = "KtorClient"

        private const val CONNECTION_TIMEOUT_SECONDS = 10L
        private const val TIMEOUT_SECONDS = 30L

        private const val MAX_IDLE_CONNECTIONS = 5
        private const val KEEP_ALIVE_DURATION = 5L
    }

    private val json = Json {
        prettyPrint = true
        isLenient = true
        ignoreUnknownKeys = true
    }

    private var _httpClient: HttpClient? = null

    fun getHttpClient(): HttpClient {
        // 기존 클라이언트가 없거나 토큰이 변경된 경우 새로 생성
        if (_httpClient == null) {
            _httpClient = createHttpClient()
        }
        return _httpClient!!
    }

    // 토큰 변경 시 클라이언트 재생성을 위한 메서드
    fun recreateHttpClient() {
        _httpClient?.close()  // 기존 클라이언트 종료
        _httpClient = null    // 다음 호출 시 새로 생성되도록
    }

    fun createHttpClient(): HttpClient {
        return HttpClient(OkHttp) {
            engine {
                config {
                    val loggingInterceptor = HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    }
                    addInterceptor(loggingInterceptor)

                    connectionPool(
                        ConnectionPool(
                            MAX_IDLE_CONNECTIONS,
                            KEEP_ALIVE_DURATION,
                            TimeUnit.MINUTES
                        )
                    )

                    connectTimeout(CONNECTION_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                    readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                    writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)

                    followRedirects(true)
                    retryOnConnectionFailure(true)
                }
            }

            expectSuccess = false

            install(ContentNegotiation) {
                json(json)
            }

            install(HttpTimeout) {
                connectTimeoutMillis = CONNECTION_TIMEOUT_SECONDS * 1500
                requestTimeoutMillis = TIMEOUT_SECONDS * 2000
                socketTimeoutMillis = TIMEOUT_SECONDS * 1500
            }

            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        Log.d(TAG, message)
                    }
                }
                level = LogLevel.HEADERS
            }

            install(Auth) {
                bearer {
                    loadTokens {
                        val token = runBlocking { authLocalDataSource.getAuthToken() }
                        if (token != null) {
                            Log.d(TAG, "Adding auth token to request")
                            BearerTokens(
                                accessToken = token.accessToken,
                                refreshToken = token.refreshToken
                            )
                        } else {
                            Log.d(TAG, "No auth token available")
                            null
                        }
                    }

                    refreshTokens {
                        try {
                            Log.d(TAG, "Refreshing auth token")
                            refreshTokenHandler(oldTokens)
                        } catch (e: Exception) {
                            Log.e(TAG, "Token refresh failed", e)
                            // 토큰 갱신 실패 시 로그아웃 처리
                            runBlocking {
                                authEventBus.emit(AuthEvent.TokenRefreshFailed)
                            }
                            null
                        }
                    }

                    sendWithoutRequest { request ->
                        // 로그인, 토큰 갱신 같은 인증 관련 엔드포인트는 제외
                        val authExcluded = request.url.pathSegments.lastOrNull() == "login" || request.url.pathSegments.contains("chat")
                        !authExcluded
                    }
                }
            }

            defaultRequest {
                contentType(ContentType.Application.Json)
                accept(ContentType.Application.Json)
            }
        }
    }

    fun createHttpClientForRefresh(): HttpClient {
        return HttpClient(OkHttp) {
            engine {
                config {
                    followRedirects(true)
                    connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                    readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                    writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                }
            }

            install(ContentNegotiation) {
                json(json)
            }
            install(Logging) {
                level = LogLevel.BODY
            }
        }
    }

    private fun refreshTokenHandler(oldTokens: BearerTokens?): BearerTokens? {
        val refreshToken = oldTokens?.refreshToken ?: return null
        Log.d(TAG, "Token expired, refreshing with refresh token")

        val refreshClient = createHttpClientForRefresh()
        val currentBaseUrl = runBlocking { baseUrlManager.getBaseUrl() }

        val refreshResponse = runBlocking {
            refreshClient.use { client ->
                client.post("$currentBaseUrl/auth/token/refresh") {
                    contentType(ContentType.Application.Json)
                    setBody(RefreshTokenRequest(refreshToken = refreshToken))
                }
            }
        }

        val tokenResponseDto = runBlocking {
            refreshResponse.body<RefreshResponse>()
        }

        runBlocking {
            val newAccessToken = tokenResponseDto.accessToken
            authLocalDataSource.saveAuthToken(AuthToken(newAccessToken, oldTokens.refreshToken))
            Log.d(TAG, "Token refreshed successfully")
        }

        return BearerTokens(
            accessToken = tokenResponseDto.accessToken,
            refreshToken = oldTokens.refreshToken
        )
    }
}