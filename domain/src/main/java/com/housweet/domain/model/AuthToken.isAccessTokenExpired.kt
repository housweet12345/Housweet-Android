package com.housweet.domain.model

import java.util.Base64
import org.json.JSONObject

fun AuthToken.isAccessTokenExpired(): Boolean {
    return try {
        val parts = this.accessToken.split(".")
        if (parts.size != 3) return true

        val payloadBytes = Base64.getUrlDecoder().decode(parts[1])
        val payloadJson = String(payloadBytes, Charsets.UTF_8)
        val payload = JSONObject(payloadJson)

        val exp = payload.getLong("exp") * 1000 // 초 → 밀리초
        val now = System.currentTimeMillis()

        now >= exp
    } catch (e: Exception) {
        true
    }
}