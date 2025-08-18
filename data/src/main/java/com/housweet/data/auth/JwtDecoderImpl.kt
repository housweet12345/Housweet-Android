package com.housweet.data.auth

import android.util.Base64
import com.housweet.domain.auth.JwtDecoder
import org.json.JSONObject
import javax.inject.Inject

class JwtDecoderImpl @Inject constructor() : JwtDecoder {
    override fun getUserIdFrom(token: String): Int? = try {
        val payload = token.split(".").getOrNull(1) ?: return null
        val json = String(Base64.decode(payload, Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP))
        val obj = JSONObject(json)
        when {
            obj.has("user_id") -> obj.getInt("user_id")
            obj.has("id") -> obj.getInt("id")
            else -> null
        }
    } catch (_: Exception) { null }
}