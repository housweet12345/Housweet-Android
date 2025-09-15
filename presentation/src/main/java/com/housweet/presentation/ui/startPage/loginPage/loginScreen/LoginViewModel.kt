package com.housweet.presentation.ui.startPage.loginPage.loginScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.housweet.domain.usecase.start.IsBelongToRoomUseCase
import com.housweet.domain.usecase.start.IsSetProfileUseCase
import com.housweet.domain.usecase.start.LoginWithKakaoUseCase
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
    private val loginWithKakaoUseCase: LoginWithKakaoUseCase,
    private val isSetProfileUseCase: IsSetProfileUseCase,
    private val isBelongToRoomUseCase: IsBelongToRoomUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<LoginEvent>()
    val event = _event.asSharedFlow()

    fun login(socialId: String, accessToken: String, email: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = loginWithKakaoUseCase(
                socialId = socialId,
                accessToken = accessToken,
                email = email
            )
            result.onSuccess { isTermsOfServiceAgreed ->
                isSetProfile(isTermsOfServiceAgreed)
            }
            result.onFailure {
                loginFail()
            }
        }
    }

    private suspend fun isSetProfile(isTermsOfServiceAgreed: Boolean) {
        val result = isSetProfileUseCase()
        result.onSuccess { isSetProfile ->
            isBelongToRoom(isSetProfile, isTermsOfServiceAgreed)
        }

        result.onFailure { e ->
            e.printStackTrace()
            loginFail()
        }
    }

    private suspend fun isBelongToRoom(isSetProfile: Boolean, isTermsOfServiceAgreed: Boolean) {
        val result = isBelongToRoomUseCase()
        result.onSuccess { isBelongToRoom ->
            loginSuccess(isTermsOfServiceAgreed, isSetProfile, isBelongToRoom)
        }

        result.onFailure { e ->
            e.printStackTrace()
            loginFail()
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

    private suspend fun loginSuccess(isTermsOfServiceAgreed: Boolean, isSetProfile: Boolean, isBelongToRoom: Boolean) {
        _event.emit(LoginEvent.LoginSuccess(isTermsOfServiceAgreed, isSetProfile, isBelongToRoom))
    }
}