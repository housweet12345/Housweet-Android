package com.housweet.data.network

import android.util.Log
import com.housweet.data.response.ChatUserResponse
import com.housweet.data.response.ChatUsersEnvelope
import com.housweet.data.response.CreateChatRoomResponse
import com.housweet.data.response.ChatMessageResponse
import com.housweet.data.response.SendMessageResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import okhttp3.ConnectionPool
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ChatRemoteDataSourceImpl @Inject constructor(
    // 주입되는 authed client는 다른 용처가 있을 수 있으니 유지 (미사용 경고만 납니다)
    @Suppress("unused")
    private val authedClient: HttpClient
) : ChatRemoteDataSource {

    private val base = "http://54.180.30.121:8000"
    private val TAG = "KtorClient"

    // ✅ 토큰 플러그인 없는 전용 HttpClient
    private val noAuthClient: HttpClient by lazy {
        HttpClient(OkHttp) {
            engine {
                config {
                    connectionPool(ConnectionPool(5, 5, TimeUnit.MINUTES))
                    followRedirects(true)
                }
            }
            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                        isLenient = true
                    }
                )
            }
            install(HttpTimeout) {
                connectTimeoutMillis = 15_000
                requestTimeoutMillis = 60_000
                socketTimeoutMillis = 45_000
            }
            // ⚠️ java.util.logging.Logger 와 충돌 방지: fully-qualified 사용
            install(io.ktor.client.plugins.logging.Logging) {
                logger = object : io.ktor.client.plugins.logging.Logger {
                    override fun log(message: String) {
                        Log.d(TAG, message)
                    }
                }
                level = io.ktor.client.plugins.logging.LogLevel.HEADERS
            }
            // 확장함수(contentType/accept) 대신 직접 헤더 추가 (충돌 방지)
            defaultRequest {
                headers.append(HttpHeaders.ContentType, "application/json")
                headers.append(HttpHeaders.Accept, "application/json")
            }
        }
    }

    // 공통: 안전 파서 (한 번만 선언!)
    private suspend inline fun <reified T> HttpResponse.safeBodyOrNull(): T? =
        runCatching { this.body<T>() }
            .onFailure {
                Log.e(
                    TAG,
                    "JSON parse failed (${this.status}): " +
                            runCatching { bodyAsText() }.getOrNull(),
                    it
                )
            }.getOrNull()

    override suspend fun getChatUsers(senderId: Int): List<ChatUserResponse> {
        return try {
            val res: HttpResponse = noAuthClient.get("$base/chat/view-room/$senderId/")
            if (res.status.isSuccess()) {
                res.safeBodyOrNull<ChatUsersEnvelope>()?.results ?: emptyList()
            } else {
                Log.e(TAG, "getChatUsers failed ${res.status}: ${res.bodyAsText()}")
                emptyList()
            }
        } catch (e: Exception) {
            Log.e(TAG, "getChatUsers exception", e)
            emptyList()
        }
    }

    override suspend fun sendMessage(senderId: Int, receiverId: Int, message: String): Boolean {
        return try {
            val res: HttpResponse = noAuthClient.post("$base/chat/send/$senderId/$receiverId/") {
                setBody(mapOf("message" to message))
            }
            when {
                res.status == HttpStatusCode.NoContent -> true
                res.status.isSuccess() -> res.safeBodyOrNull<SendMessageResponse>()?.success ?: true
                else -> {
                    Log.e(TAG, "sendMessage failed ${res.status}: ${res.bodyAsText()}")
                    false
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "sendMessage exception", e)
            false
        }
    }

    override suspend fun getChatMessages(senderId: Int, receiverId: Int): List<ChatMessageResponse> {
        return try {
            val res: HttpResponse = noAuthClient.get("$base/chat/$senderId/$receiverId/messages/")
            if (res.status.isSuccess()) {
                res.safeBodyOrNull<List<ChatMessageResponse>>() ?: emptyList()
            } else {
                Log.e(TAG, "getChatMessages failed ${res.status}: ${res.bodyAsText()}")
                emptyList()
            }
        } catch (e: Exception) {
            Log.e(TAG, "getChatMessages exception", e)
            emptyList()
        }
    }

    override suspend fun deleteRoom(roomId: Int): Result<Unit> {
        return try {
            val res: HttpResponse = noAuthClient.post("$base/chat/room/$roomId/delete/")
            if (res.status.isSuccess()) Result.success(Unit)
            else Result.failure(IllegalStateException("Delete failed (${res.status.value}) ${res.bodyAsText()}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun reportRoom(roomId: Int): Result<Unit> {
        return try {
            val res: HttpResponse = noAuthClient.post("$base/chat/block/$roomId/")
            if (res.status.isSuccess()) Result.success(Unit)
            else Result.failure(IllegalStateException("Report failed (${res.status.value}) ${res.bodyAsText()}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun createRoom(senderId: Int, receiverId: Int): Result<Int> {
        return try {
            val res: HttpResponse = noAuthClient.post("$base/chat/create_room/$senderId/$receiverId/")
            if (res.status.isSuccess()) {
                val body: CreateChatRoomResponse? = res.safeBodyOrNull()
                if (body?.created == true) Result.success(body.room_id)
                else Result.failure(IllegalStateException("Room not created (missing/invalid body)"))
            } else {
                Result.failure(IllegalStateException("Create room failed (${res.status.value}) ${res.bodyAsText()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}