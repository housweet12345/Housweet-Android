package com.housweet.presentation.ui.startPage.loginPage.loginScreen

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.housweet.domain.repository.AuthRepository
import com.housweet.domain.usecase.UseCases
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val useCases: UseCases
) : ViewModel() {
    private val _loginUiState  = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val loginUiState = _loginUiState.asStateFlow()

    fun loginFail() {
        _loginUiState.value = LoginUiState.LoginError
    }

    fun isLoading() {
        _loginUiState.value = LoginUiState.IsLoading
    }

    fun signUp() {
        _loginUiState.value = LoginUiState.SignUp
    }

    fun signIn() {
        _loginUiState.value = LoginUiState.SignIn
    }
}