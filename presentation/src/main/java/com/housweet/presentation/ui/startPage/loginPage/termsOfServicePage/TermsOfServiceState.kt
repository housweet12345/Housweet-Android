package com.housweet.presentation.ui.startPage.loginPage.termsOfServicePage

sealed interface TermsOfServiceState {
    data object Idle : TermsOfServiceState
    data object IsLoading: TermsOfServiceState
}

sealed interface TermsOfServiceEvent {
    data object Success : TermsOfServiceEvent
    data object Error : TermsOfServiceEvent
}