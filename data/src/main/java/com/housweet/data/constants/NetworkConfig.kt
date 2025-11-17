package com.housweet.data.constants

import java.util.concurrent.TimeUnit

/**
 * 네트워크 관련 설정 상수 관리 객체
 * Timeout, Connection Pool 등 네트워크 설정을 중앙에서 관리합니다.
 */
object NetworkConfig {
    /**
     * 일반 HTTP Client Timeout 설정 (초 단위)
     */
    object Timeout {
        const val CONNECTION_SECONDS = 10L
        const val READ_WRITE_SECONDS = 30L

        /**
         * Ktor Client Timeout 설정 (밀리초 단위)
         */
        const val CONNECTION_MILLIS = CONNECTION_SECONDS * 1500
        const val REQUEST_MILLIS = READ_WRITE_SECONDS * 2000
        const val SOCKET_MILLIS = READ_WRITE_SECONDS * 1500
    }

    /**
     * Chat 전용 Timeout 설정 (밀리초 단위)
     */
    object ChatTimeout {
        const val CONNECTION_MILLIS = 15_000L
        const val REQUEST_MILLIS = 60_000L
        const val SOCKET_MILLIS = 45_000L
    }

    /**
     * Connection Pool 설정
     */
    object ConnectionPool {
        const val MAX_IDLE_CONNECTIONS = 5
        const val KEEP_ALIVE_DURATION_MINUTES = 5L

        /**
         * TimeUnit.MINUTES를 반환하는 헬퍼 속성
         */
        val KEEP_ALIVE_TIME_UNIT: TimeUnit = TimeUnit.MINUTES
    }
}
