package com.housweet.data.utils

import android.util.Base64
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

object TokenUtils {
    
    /**
     * JWT 토큰에서 userId를 추출하는 함수
     * @param token JWT access token
     * @return userId 또는 null (파싱 실패시)
     */
    fun getUserIdFromToken(token: String): Int? {
        return try {
            // JWT는 header.payload.signature 형태
            val parts = token.split(".")
            if (parts.size != 3) return null
            
            val payload = parts[1]
            
            // Base64 디코딩 (패딩 추가 필요할 수 있음)
            val decodedBytes = Base64.decode(addPadding(payload), Base64.URL_SAFE)
            val payloadJson = String(decodedBytes)
            
            // JSON 파싱하여 userId 추출
            val jsonElement = Json.parseToJsonElement(payloadJson)
            val userId = jsonElement.jsonObject["userId"]?.jsonPrimitive?.content
                ?: jsonElement.jsonObject["user_id"]?.jsonPrimitive?.content
                ?: jsonElement.jsonObject["sub"]?.jsonPrimitive?.content
            
            userId?.toIntOrNull()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    
    /**
     * Base64 URL-safe 디코딩을 위한 패딩 추가
     */
    private fun addPadding(base64: String): String {
        val remainder = base64.length % 4
        return if (remainder > 0) {
            base64 + "=".repeat(4 - remainder)
        } else {
            base64
        }
    }
}