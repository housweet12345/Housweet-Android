package com.housweet.presentation.ui.navigation
import kotlinx.serialization.Serializable

sealed interface Route {
    sealed interface StartPageRoute: Route {
        sealed interface LoginRoute : StartPageRoute {
            @Serializable
            data object Login : LoginRoute

            @Serializable
            data object WelCome : LoginRoute

            @Serializable
            data object PermissionGuide : LoginRoute
        }

        sealed interface AccessRoomRoute : StartPageRoute {
            @Serializable
            data object AccessRoom : AccessRoomRoute

            @Serializable
            data object CreateRoom : AccessRoomRoute

            @Serializable
            data object SearchRoom : AccessRoomRoute
        }
    }

    sealed interface CommunityPageRoute: Route {
        @Serializable
        data object Map : CommunityPageRoute

        @Serializable
        data object SearchRegion : CommunityPageRoute

        sealed interface PostRoute : CommunityPageRoute {
            @Serializable
            data object DetailPost : PostRoute

            @Serializable
            data object Posts: PostRoute
        }
    }
}