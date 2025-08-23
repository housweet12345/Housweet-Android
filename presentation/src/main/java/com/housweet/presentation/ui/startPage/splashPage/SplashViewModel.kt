package com.housweet.presentation.ui.startPage.splashPage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    private val isBelongToRoomUseCase: IsBelongToRoomUseCase
): ViewModel() {
    private val _uiState = MutableStateFlow<SplashState>(SplashState.Idle)
    val uiState = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<SplashEvent>()
    val event = _event.asSharedFlow()

    init {
        checkLogin()
    }

    fun checkLogin() {
        viewModelScope.launch {
            checkLoginUseCase().collect {
                delay(1500)
                it.onSuccess { isAutoLogin ->
                    if (isAutoLogin) isTermsOfServiceAgreed()
                    else _event.emit(SplashEvent.IsNotAutoLogin)
                }
                it.onFailure { e ->
                    e.printStackTrace()
                    _event.emit(SplashEvent.Error)
                }
            }
        }
    }

    private suspend fun isTermsOfServiceAgreed() {
        isTermsOfServiceAgreedUseCase().collect {
            it.onSuccess { isAgreeTermsOfService ->
                isProfileSet(isAgreeTermsOfService)
            }
            it.onFailure { e ->
                e.printStackTrace()
                _event.emit(SplashEvent.Error)
            }
        }
    }

    private suspend fun isProfileSet(isAgreeTermsOfService: Boolean) {
        isSetProfileUseCase().collect {
            it.onSuccess { isSetProfile ->
                isBelongToRoom(isAgreeTermsOfService, isSetProfile)
            }

            it.onFailure { e ->
                e.printStackTrace()
                _event.emit(SplashEvent.Error)
            }
        }
    }

    private suspend fun isBelongToRoom(isAgreeTermsOfService: Boolean, isSetProfile: Boolean) {
        isBelongToRoomUseCase().collect {
            it.onSuccess { isBelongToRoom ->
                _event.emit(
                    SplashEvent.IsAutoLogin(isAgreeTermsOfService, isSetProfile, isBelongToRoom)
                )
            }

            it.onFailure { e ->
                e.printStackTrace()
                _event.emit(SplashEvent.Error)
            }
        }
    }
}
