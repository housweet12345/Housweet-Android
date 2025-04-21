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

    sealed interface AccessRoomRoute : Route {
        @Serializable
        data object AccessRoomScreen : AccessRoomRoute

        @Serializable
        data object CreateRoomScreen : AccessRoomRoute

        @Serializable
        data object SearchRoomScreen : AccessRoomRoute
    }
}