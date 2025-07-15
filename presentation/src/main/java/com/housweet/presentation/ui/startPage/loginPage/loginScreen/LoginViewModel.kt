package com.housweet.presentation.ui.startPage.loginPage.loginScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.housweet.domain.usecase.UseCases
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
            ).collect { result ->
                result.fold(
                    onSuccess = { status ->
                        if (status == 200) {
                            signIn()
                        } else {
                            signUp()
                        }
                    },
                    onFailure = {
                        Log.d("LoginViewModel", "서버 로그인 실패 : ${it.message}")
                        loginFail()
                    }
                )
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

    private suspend fun signUp() {
        _event.emit(LoginEvent.SignUp)
    }

    private suspend fun signIn() {
        _event.emit(LoginEvent.SignIn)
    }
}