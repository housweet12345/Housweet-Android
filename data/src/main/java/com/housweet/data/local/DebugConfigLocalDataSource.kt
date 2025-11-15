package com.housweet.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DebugConfigLocalDataSource @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        private val BASE_URL_KEY = stringPreferencesKey("debug_base_url")
        private val USER_BASE_URL_KEY = stringPreferencesKey("debug_user_base_url")
    }

    fun getBaseUrl(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[BASE_URL_KEY]
        }
    }

    fun getUserBaseUrl(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[USER_BASE_URL_KEY]
        }
    }

    suspend fun getBaseUrlSync(): String? {
        return dataStore.data.first()[BASE_URL_KEY]
    }

    suspend fun getUserBaseUrlSync(): String? {
        return dataStore.data.first()[USER_BASE_URL_KEY]
    }

    suspend fun setBaseUrl(url: String) {
        dataStore.edit { preferences ->
            preferences[BASE_URL_KEY] = url
        }
    }

    suspend fun setUserBaseUrl(url: String) {
        dataStore.edit { preferences ->
            preferences[USER_BASE_URL_KEY] = url
        }
    }

    suspend fun clearUrls() {
        dataStore.edit { preferences ->
            preferences.remove(BASE_URL_KEY)
            preferences.remove(USER_BASE_URL_KEY)
        }
    }
}