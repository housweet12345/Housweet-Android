package com.housweet.presentation.viewmodel.mypage

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.housweet.domain.repository.MyHouseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface MyHouseEditEffect {
    data class ShowMessage(val msg: String) : MyHouseEditEffect
    object CloseWithRefresh : MyHouseEditEffect
}

data class MyHouseEditUiState(
    val roomId: Int? = null,
    val name: String = "",
    val startDate: String = "",
    val inviteCode: String = "",
    val isLoading: Boolean = false
)


@HiltViewModel
class MyHouseEditViewModel @Inject constructor(
    private val repo: MyHouseRepository,
    @Suppress("unused") private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(MyHouseEditUiState())
    val uiState: StateFlow<MyHouseEditUiState> = _uiState

    private val _effect = MutableSharedFlow<MyHouseEditEffect>(extraBufferCapacity = 1)
    val effect: SharedFlow<MyHouseEditEffect> = _effect.asSharedFlow()

    fun load() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            runCatching { repo.getMyHouse() }
                .onSuccess { house ->
                    Log.d("MyHouseEditVM", "GET success: id=${house.id}, name=${house.name}")
                    _uiState.value = MyHouseEditUiState(
                        roomId = house.id,
                        name = house.name,
                        startDate = house.dateOfJoined,
                        inviteCode = house.inviteCode,
                        isLoading = false
                    )
                }
                .onFailure { e ->
                    Log.e("MyHouseEditVM", "GET failed", e)
                    _uiState.value = _uiState.value.copy(isLoading = false)
                    _effect.tryEmit(MyHouseEditEffect.ShowMessage(e.message ?: "불러오기 실패"))
                }
        }
    }

    fun onNameChange(value: String) {
        if (value.length <= 10) {
            _uiState.value = _uiState.value.copy(name = value)
        }
    }

    fun submit() {
        if (_uiState.value.isLoading) return

        val roomId = _uiState.value.roomId ?: run {
            _effect.tryEmit(MyHouseEditEffect.ShowMessage("방 정보를 불러오는 중입니다"))
            return
        }
        val name = _uiState.value.name.trim()
        if (name.isEmpty()) {
            _effect.tryEmit(MyHouseEditEffect.ShowMessage("이름을 입력하세요"))
            return
        }

        viewModelScope.launch {
            Log.d("MyHouseEditVM", "PATCH start: roomId=$roomId, name=$name")
            _uiState.value = _uiState.value.copy(isLoading = true)
            runCatching { repo.updateMyHouseName(roomId, name) }
                .onSuccess { updated ->
                    Log.d("MyHouseEditVM", "PATCH success: id=${updated.id}, name=${updated.name}")
                    _uiState.value = _uiState.value.copy(
                        roomId = updated.id,
                        name = updated.name,
                        startDate = updated.dateOfJoined,
                        inviteCode = updated.inviteCode,
                        isLoading = false
                    )
                    _effect.tryEmit(MyHouseEditEffect.CloseWithRefresh)
                }
                .onFailure { e ->
                    Log.e("MyHouseEditVM", "PATCH failed", e)
                    _uiState.value = _uiState.value.copy(isLoading = false)
                    _effect.tryEmit(MyHouseEditEffect.ShowMessage(e.message ?: "수정 실패"))
                }
        }
    }
}