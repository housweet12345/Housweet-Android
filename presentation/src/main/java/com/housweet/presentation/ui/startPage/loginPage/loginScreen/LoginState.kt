package com.housweet.presentation.ui.startPage.loginPage.loginScreen

sealed interface LoginUiState {
    data object Idle : LoginUiState
    data object IsLoading: LoginUiState
}

sealed interface LoginEvent {
    data object SignIn : LoginEvent
    data object SignUp : LoginEvent
    data object LoginError : LoginEvent
}