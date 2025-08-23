package com.housweet.presentation.ui.mypage.deleteAccount

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.housweet.domain.usecase.DeleteAccountUseCase
import com.housweet.domain.usecase.start.IsBelongToRoomUseCase
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeleteAccountViewModel @Inject constructor(
    private val isBelongToRoomUseCase: IsBelongToRoomUseCase,
    private val deleteAccountUseCase: DeleteAccountUseCase
): ViewModel() {
    private val _uiState = MutableStateFlow<DeleteAccountUiState>(DeleteAccountUiState.Idle)
    val uiState = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<DeleteAccountEvent>()
    val event = _event.asSharedFlow()

    init {
        isBelongToRoom()
    }

    private fun isBelongToRoom() {
        _uiState.value = DeleteAccountUiState.IsLoading
        viewModelScope.launch {
            isBelongToRoomUseCase().collect {
                _uiState.value = DeleteAccountUiState.Idle
                it.onSuccess { isBelongToRoom ->
                    _event.emit(DeleteAccountEvent.IsBelongToRoom(isBelongToRoom))
                }

                it.onFailure { e ->
                    e.printStackTrace()
                    _event.emit(DeleteAccountEvent.Error("방의 가입 여부를 불러오는데 실패했어요."))
                }
            }
        }
    }

    fun deleteAccount() {
        _uiState.value = DeleteAccountUiState.IsLoading
        UserApiClient.instance.unlink { error ->
            if (error != null) {
                _uiState.value = DeleteAccountUiState.Idle
                viewModelScope.launch { _event.emit(DeleteAccountEvent.Error("회원 탙퇴에 실패했어요.")) }
                return@unlink
            }

            viewModelScope.launch {
                deleteAccountUseCase().collect {
                    _uiState.value = DeleteAccountUiState.Idle
                    it.onSuccess { successDelete ->
                        if (successDelete) _event.emit(DeleteAccountEvent.DeleteAccountSuccess)
                        else _event.emit(DeleteAccountEvent.Error("회원 탙퇴에 실패했어요."))
                    }

                    it.onFailure { e ->
                        e.printStackTrace()
                        _event.emit(DeleteAccountEvent.Error("회원 탙퇴에 실패했어요."))
                    }
                }
            }
        }
    }
}