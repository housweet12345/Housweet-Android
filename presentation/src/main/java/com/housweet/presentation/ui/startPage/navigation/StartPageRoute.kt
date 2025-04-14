package com.housweet.presentation.ui.startPage.navigation
import kotlinx.serialization.Serializable

sealed interface Route {
    sealed interface LoginRoute : Route {
        @Serializable
        data object LoginScreen : LoginRoute

        @Serializable
        data object WelComeScreen: LoginRoute

        @Serializable
        data object PermissionGuideScreen : LoginRoute
    }
}