package com.housweet.data.network.dto

import android.util.Log
import androidx.compose.runtime.Immutable
import com.housweet.domain.model.AuthToken
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.longOrNull
import java.util.Base64

@Serializable
@Immutable
data class LoginResponseDto(
    @SerialName("access")
    val accessToken: String,

    @SerialName("refresh")
    val refreshToken: String,
)

fun LoginResponseDto.toAuthToken(): AuthToken {
    val parts = accessToken.split(".")
    val decoder = Base64.getUrlDecoder()
    val payload = String(decoder.decode(parts[1]))

    val jsonObject = Json.parseToJsonElement(payload) as JsonObject
    val exp = jsonObject["exp"]?.jsonPrimitive?.longOrNull ?: ((System.currentTimeMillis() / 1000) + 1800)

    return AuthToken(
        accessToken = accessToken,
        refreshToken = refreshToken,
        expiresIn = exp
    )
}