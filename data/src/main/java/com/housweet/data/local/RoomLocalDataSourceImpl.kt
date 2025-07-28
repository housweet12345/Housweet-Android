package com.housweet.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.housweet.domain.local.RoomLocalDataSource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.roomDataStore by preferencesDataStore(name = "room_preferences")

@Singleton
class RoomLocalDataSourceImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : RoomLocalDataSource {

    companion object {
        private val KEY_ROOM_ID = intPreferencesKey("room_id")
    }

    override suspend fun saveRoomId(id: Int) {
        context.roomDataStore.edit { prefs ->
            prefs[KEY_ROOM_ID] = id
        }
    }

    override suspend fun getRoomId(): Int? {
        return context.roomDataStore.data.map { prefs ->
            prefs[KEY_ROOM_ID]
        }.firstOrNull()
    }

    override suspend fun clearRoomId() {
        context.roomDataStore.edit { prefs ->
            prefs.remove(KEY_ROOM_ID)
        }
    }
}