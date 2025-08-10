package com.housweet.presentation.ui.startPage.splashPage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.housweet.domain.usecase.UseCases
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
    private val useCases: UseCases
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
            useCases.checkLoginUseCase().collect {
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

    private fun isTermsOfServiceAgreed() {
        viewModelScope.launch {
            useCases.isTermsOfServiceAgreedUseCase().collect {
                it.onSuccess { isAgreeTermsOfService ->
                    isBelongToRoom(isAgreeTermsOfService)
                }
                it.onFailure { e ->
                    e.printStackTrace()
                    _event.emit(SplashEvent.Error)
                }
            }
        }
    }

    private fun isBelongToRoom(isAgreeTermsOfService: Boolean) {
        viewModelScope.launch {
            useCases.isBelongToRoomUseCase().collect {
                it.onSuccess { isBelongToRoom ->
                    _event.emit(
                        SplashEvent.IsAutoLogin(isAgreeTermsOfService, isBelongToRoom)
                    )
                }

                it.onFailure { e ->
                    e.printStackTrace()
                    _event.emit(SplashEvent.Error)
                }
            }
        }
    }
}
