package com.housweet.presentation.ui.startPage.loginPage.loginScreen

sealed interface LoginUiState {
    data object IDlE : LoginUiState
    data object SignIn : LoginUiState
    data object SignUp : LoginUiState
    data object LoginError : LoginUiState
    data object IsLoading: LoginUiState
}