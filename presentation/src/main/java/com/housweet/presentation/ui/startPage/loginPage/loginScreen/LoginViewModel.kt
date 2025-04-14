package com.housweet.presentation.ui.startPage.loginPage.loginScreen

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {
    private val _loginUiState  = MutableStateFlow<LoginUiState>(LoginUiState.IDlE)
    val loginUiState = _loginUiState.asStateFlow()

    private fun loginFail() {
        _loginUiState.value = LoginUiState.LoginError
    }

    private fun isLoading() {
        _loginUiState.value = LoginUiState.IsLoading
    }

    private fun signUp() {
        _loginUiState.value = LoginUiState.SignUp
    }

    private fun signIn() {
        _loginUiState.value = LoginUiState.SignIn
    }

    fun kakaoLogin() {
        isLoading()
        val kakaoLoginCallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                loginFail()
            } else if (token != null) {
                signUp()
            }
        }

        UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
            if (error != null) {
                if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                    loginFail()
                    return@loginWithKakaoTalk
                }
                UserApiClient.instance.loginWithKakaoAccount(context, callback = kakaoLoginCallback)
            } else if (token != null) {
                signUp()
            }
        }
    }
}