package com.housweet.presentation.ui.startPage.loginPage.loginScreen

sealed interface LoginUiState {
    data object Idle : LoginUiState
    data object IsLoading: LoginUiState
}

sealed interface LoginEvent {
    data class LoginSuccess(val isTermsOfServiceAgreed: Boolean, val isBelongToRoom: Boolean) : LoginEvent
    data object LoginError : LoginEvent
}