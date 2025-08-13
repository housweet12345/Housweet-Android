package com.housweet.presentation.ui.startPage.loginPage.loginScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.housweet.domain.usecase.UseCases
import com.housweet.presentation.ui.startPage.splashPage.SplashEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val useCases: UseCases
) : ViewModel() {
    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<LoginEvent>()
    val event = _event.asSharedFlow()

    fun login(socialId: String, accessToken: String, email: String) {
        viewModelScope.launch(Dispatchers.IO) {
            useCases.loginWithKakaoUseCase(
                socialId = socialId,
                accessToken = accessToken,
                email = email
            ).collect {
                it.onSuccess { isTermsOfServiceAgreed ->
                    isBelongToRoom(isTermsOfServiceAgreed)
                }
                it.onFailure {
                    loginFail()
                }
            }
        }
    }

    private suspend fun isBelongToRoom(isTermsOfServiceAgreed: Boolean) {
        useCases.isBelongToRoomUseCase().collect {
            it.onSuccess { isBelongToRoom ->
                loginSuccess(isTermsOfServiceAgreed, isBelongToRoom)
            }

            it.onFailure { e ->
                e.printStackTrace()
            }
        }
    }

    fun isLoading() {
        _uiState.value = LoginUiState.IsLoading
    }

    fun loginFail() {
        viewModelScope.launch {
            isIdle()
            _event.emit(LoginEvent.LoginError)
        }
    }

    private fun isIdle() {
        _uiState.value = LoginUiState.Idle
    }

    private suspend fun loginSuccess(isTermsOfServiceAgreed: Boolean ,isBelongToRoom: Boolean) {
        _event.emit(LoginEvent.LoginSuccess(isTermsOfServiceAgreed, isBelongToRoom))
    }
}