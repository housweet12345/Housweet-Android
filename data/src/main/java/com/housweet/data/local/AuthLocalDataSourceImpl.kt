package com.housweet.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.housweet.domain.model.AuthToken
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthLocalDataSourceImpl @Inject constructor(
    @ApplicationContext private val context: Context
): AuthLocalDataSource {

    companion object {
        private val KEY_ACCESS_TOKEN = stringPreferencesKey("access_token")
        private val KEY_REFRESH_TOKEN = stringPreferencesKey("refresh_token")
        private val KEY_EXPIRES_IN = longPreferencesKey("expires_in")
        private val KEY_TOKEN_EXPIRATION_TIME = longPreferencesKey("token_expiration_time")
    }

    private val Context.authDataStore by preferencesDataStore(name = "auth_preferences")

    override suspend fun saveAuthToken(token: AuthToken) {
        val expirationTime = System.currentTimeMillis() + (token.expiresIn * 1000)
        context.authDataStore.edit { preferences ->
            preferences[KEY_ACCESS_TOKEN] = token.accessToken
            preferences[KEY_REFRESH_TOKEN] = token.refreshToken
            preferences[KEY_EXPIRES_IN] = token.expiresIn
            preferences[KEY_TOKEN_EXPIRATION_TIME] = expirationTime
        }
    }

    override suspend fun getAuthToken(): AuthToken? {
        return context.authDataStore.data.map { preferences ->
            val accessToken = preferences[KEY_ACCESS_TOKEN] ?: return@map null
            val refreshToken = preferences[KEY_REFRESH_TOKEN] ?: return@map null
            val expiresIn = preferences[KEY_EXPIRES_IN] ?: 0

            AuthToken(accessToken, refreshToken, expiresIn)
        }.firstOrNull()
    }

    override suspend fun clearAuthToken() {
        context.authDataStore.edit { preferences ->
            preferences.remove(KEY_ACCESS_TOKEN)
            preferences.remove(KEY_REFRESH_TOKEN)
            preferences.remove(KEY_EXPIRES_IN)
            preferences.remove(KEY_TOKEN_EXPIRATION_TIME)
        }
    }

    override suspend fun isTokenExpired(): Boolean {
        val expirationTime = context.authDataStore.data.map { preferences ->
            preferences[KEY_TOKEN_EXPIRATION_TIME] ?: 0L
        }.firstOrNull() ?: 0L

        return System.currentTimeMillis() >= expirationTime
    }
}