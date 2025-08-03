package com.housweet.presentation.ui.navigation
import com.housweet.domain.model.Coordinate
import kotlinx.serialization.Serializable

sealed interface Route {
    sealed interface StartPageRoute: Route {
        @Serializable
        data object Splash : StartPageRoute

        sealed interface LoginRoute : StartPageRoute {
            @Serializable
            data object Login : LoginRoute

            @Serializable
            data object WelCome : LoginRoute

            @Serializable
            data object PermissionGuide : LoginRoute

            @Serializable
            data object TermsOfService : LoginRoute
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
        data class Map(val coordinate: Coordinate? = null) : CommunityPageRoute

        @Serializable
        data object SearchRegion : CommunityPageRoute

        sealed interface PostRoute : CommunityPageRoute {
            @Serializable
            data class DetailPost(val postId: Int? = null) : PostRoute

            @Serializable
            data class Posts(val postRegions: String? = null): PostRoute
        }
    }
}