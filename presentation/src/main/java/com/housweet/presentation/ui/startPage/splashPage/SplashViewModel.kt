package com.housweet.presentation.ui.startPage.splashPage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.housweet.domain.usecase.debug.GetDebugConfigUseCase
import com.housweet.domain.usecase.start.CheckLoginUseCase
import com.housweet.domain.usecase.start.IsBelongToRoomUseCase
import com.housweet.domain.usecase.start.IsSetProfileUseCase
import com.housweet.domain.usecase.start.IsTermsOfServiceAgreedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val checkLoginUseCase: CheckLoginUseCase,
    private val isTermsOfServiceAgreedUseCase: IsTermsOfServiceAgreedUseCase,
    private val isSetProfileUseCase: IsSetProfileUseCase,
    private val isBelongToRoomUseCase: IsBelongToRoomUseCase,
    private val getDebugConfigUseCase: GetDebugConfigUseCase
): ViewModel() {
    private val _uiState = MutableStateFlow<SplashState>(
        if (getDebugConfigUseCase.isDebugMode()) SplashState.ShowDebugConfig else SplashState.Idle
    )
    val uiState = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<SplashEvent>()
    val event = _event.asSharedFlow()

    fun checkLogin() {
        viewModelScope.launch {
            checkLoginInternal()
        }
    }

    fun onDebugConfigShown() {
        _uiState.value = SplashState.Idle
    }

    private suspend fun checkLoginInternal() {
        delay(1500)
        val result = checkLoginUseCase()
        result.onSuccess { isAutoLogin ->
            if (isAutoLogin) isTermsOfServiceAgreed()
            else _event.emit(SplashEvent.IsNotAutoLogin)
        }
        result.onFailure { e ->
            e.printStackTrace()
            _event.emit(SplashEvent.Error)
        }
    }

    private suspend fun isTermsOfServiceAgreed() {
        val result = isTermsOfServiceAgreedUseCase()
        result.onSuccess { isAgreeTermsOfService ->
            isProfileSet(isAgreeTermsOfService)
        }
        result.onFailure { e ->
            e.printStackTrace()
            _event.emit(SplashEvent.Error)
        }
    }

    private suspend fun isProfileSet(isAgreeTermsOfService: Boolean) {
        val result = isSetProfileUseCase()
        result.onSuccess { isSetProfile ->
            isBelongToRoom(isAgreeTermsOfService, isSetProfile)
        }
        result.onFailure { e ->
            e.printStackTrace()
            _event.emit(SplashEvent.Error)
        }
    }

    private suspend fun isBelongToRoom(isAgreeTermsOfService: Boolean, isSetProfile: Boolean) {
        val result = isBelongToRoomUseCase()
        result.onSuccess { isBelongToRoom ->
            _event.emit(
                SplashEvent.IsAutoLogin(isAgreeTermsOfService, isSetProfile, isBelongToRoom)
            )
        }
        result.onFailure { e ->
            e.printStackTrace()
            _event.emit(SplashEvent.Error)
        }
    }
}
