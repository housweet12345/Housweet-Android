package com.housweet.domain.event

sealed class AuthEvent {
    data object TokenRefreshFailed : AuthEvent()
}