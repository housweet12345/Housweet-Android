package com.housweet.data.utils

import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess

/**
 * HTTP 응답을 안전하게 JSON으로 파싱하는 확장 함수
 *
 * @param endpoint API endpoint 이름 (에러 메시지용)
 * @return 파싱된 객체
 * @throws ResponseException HTTP 상태 코드가 성공이 아니거나 파싱에 실패한 경우
 */
suspend inline fun <reified T> HttpResponse.requireJsonOrThrow(endpoint: String): T {
    if (!status.isSuccess()) {
        throw ResponseException(
            this,
            "$endpoint failed: $status ${runCatching { bodyAsText() }.getOrNull()}"
        )
    }
    return runCatching { body<T>() }.getOrElse { ex ->
        val raw = runCatching { bodyAsText() }.getOrNull()
        throw ResponseException(
            this,
            "$endpoint parse error: ${ex.message}\nraw=$raw"
        )
    }
}
