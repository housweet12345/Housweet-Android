package com.housweet.presentation.viewmodel.registerhouse

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.housweet.domain.local.RoomLocalDataSource
import kotlinx.coroutines.launch

@HiltViewModel
class HouseRegisterViewModel @Inject constructor(
    private val roomLocalDataSource: RoomLocalDataSource
) : ViewModel() {
    fun logRoomId() {
        viewModelScope.launch {
            val roomId = roomLocalDataSource.getRoomId()
            Log.d("HouseRegister", "Room ID: $roomId")
        }
    }
}