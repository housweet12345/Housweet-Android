package com.housweet.presentation.ui.startPage.splashPage
sealed interface SplashState {
    data object Idle : SplashState
}

sealed interface SplashEvent {
    data class IsAutoLogin(val isAgreeTermsOfService: Boolean) : SplashEvent
    data object IsNotAutoLogin : SplashEvent
    data object Error : SplashEvent
}