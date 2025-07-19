package com.housweet.data.local

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.housweet.data.utils.CryptoManager
import com.housweet.domain.model.AuthToken
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.longOrNull
import java.util.Base64
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthLocalDataSourceImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val cryptoManager: CryptoManager
): AuthLocalDataSource {
    companion object {
        private val KEY_ACCESS_TOKEN = stringPreferencesKey("access_token")
        private val KEY_REFRESH_TOKEN = stringPreferencesKey("refresh_token")
        private val KEY_ACCESS_EXPIRES_IN = longPreferencesKey("access_expires_in")
        private val KEY_REFRESH_EXPIRES_IN = longPreferencesKey("refresh_expires_in")
        private val KEY_ACCESS_EXPIRATION_TIME = longPreferencesKey("access_expiration_time")
        private val KEY_REFRESH_EXPIRATION_TIME = longPreferencesKey("refresh_expiration_time")
    }

    private val Context.authDataStore by preferencesDataStore(name = "auth_preferences")

    override suspend fun saveAuthToken(token: AuthToken) {

        val accessExp = getExpiresInToken(token.accessToken)
        val refreshExp = getExpiresInToken(token.refreshToken)
        val accessExpirationTime = accessExp * 1000
        val refreshExpirationTime = refreshExp * 1000

        val encryptedAccessToken = encryptToken(token.accessToken)
        val encryptedRefreshToken = encryptToken(token.refreshToken)

        context.authDataStore.edit { preferences ->
            preferences[KEY_ACCESS_TOKEN] = encryptedAccessToken
            preferences[KEY_REFRESH_TOKEN] = encryptedRefreshToken
            preferences[KEY_ACCESS_EXPIRES_IN] = accessExp
            preferences[KEY_ACCESS_EXPIRATION_TIME] = accessExpirationTime
            preferences[KEY_REFRESH_EXPIRES_IN] = refreshExp
            preferences[KEY_REFRESH_EXPIRATION_TIME] = refreshExpirationTime
        }
    }

    override suspend fun getAuthToken(): AuthToken? =
        context.authDataStore.data.map { preferences ->
            val encryptedAccessToken = preferences[KEY_ACCESS_TOKEN] ?: return@map null
            val encryptedRefreshToken = preferences[KEY_REFRESH_TOKEN] ?: return@map null

            val accessToken = decryptToken(encryptedAccessToken) ?: return@map null
            val refreshToken = decryptToken(encryptedRefreshToken) ?: return@map null

            AuthToken(accessToken, refreshToken)
        }.firstOrNull()

    override suspend fun clearAuthToken() {
        context.authDataStore.edit { preferences ->
            preferences.remove(KEY_ACCESS_TOKEN)
            preferences.remove(KEY_REFRESH_TOKEN)
            preferences.remove(KEY_ACCESS_EXPIRES_IN)
            preferences.remove(KEY_ACCESS_EXPIRATION_TIME)
            preferences.remove(KEY_REFRESH_EXPIRES_IN)
            preferences.remove(KEY_REFRESH_EXPIRATION_TIME)
        }
    }

    override suspend fun isAccessTokenExpired(): Boolean {
        val expirationTime = context.authDataStore.data.map { preferences ->
            preferences[KEY_ACCESS_EXPIRATION_TIME] ?: 0L
        }.firstOrNull() ?: 0L

        return System.currentTimeMillis() >= expirationTime
    }

    override suspend fun isRefreshTokenExpired(): Boolean {
        val expirationTime = context.authDataStore.data.map { preferences ->
            preferences[KEY_REFRESH_EXPIRATION_TIME] ?: 0L
        }.firstOrNull() ?: 0L

        return System.currentTimeMillis() >= expirationTime
    }

    private fun getExpiresInToken(token: String): Long {
        val parts = token.split(".")
        val decoder = Base64.getUrlDecoder()
        val payload = String(decoder.decode(parts[1]))

        val jsonObject = Json.parseToJsonElement(payload) as JsonObject
        val exp = jsonObject["exp"]?.jsonPrimitive?.longOrNull ?: ((System.currentTimeMillis() / 1000) + 1800)
        return exp
    }

    private fun encryptToken(token: String): String {
        return try {
            val tokenBytes = token.encodeToByteArray()
            val encryptedBytes = cryptoManager.encrypt(tokenBytes)
            Base64.getEncoder().encodeToString(encryptedBytes)
        } catch (e: Exception) {
            Log.e("AuthLocalDataSource", "토큰 암호화 실패", e)
            throw e
        }
    }

    private fun decryptToken(encryptedToken: String): String? {
        return try {
            val encryptedBytes = Base64.getDecoder().decode(encryptedToken)
            val decryptedBytes = cryptoManager.decrypt(encryptedBytes)
            String(decryptedBytes)
        } catch (e: Exception) {
            Log.e("AuthLocalDataSource", "토큰 복호화 실패", e)
            null
        }
    }
}