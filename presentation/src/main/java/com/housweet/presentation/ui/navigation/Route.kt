package com.housweet.presentation.ui.navigation
import com.housweet.domain.model.Coordinate
import com.housweet.presentation.model.RegisterModel
import kotlinx.serialization.Serializable

sealed interface Route {
    sealed interface StartPageRoute: Route {
        @Serializable
        data object Splash : StartPageRoute

        sealed interface LoginRoute : StartPageRoute {
            @Serializable
            data object Login : LoginRoute

            @Serializable
            data class WelCome(val isBelongToRoom: Boolean) : LoginRoute

            @Serializable
            data class PermissionGuide(val isBelongToRoom: Boolean) : LoginRoute

            @Serializable
            data class TermsOfService(val isBelongToRoom: Boolean) : LoginRoute
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

    sealed interface HouseRegisterRoute : Route {
        @Serializable
        data class Step1(val mode: RegisterModel = RegisterModel.CREATE) : HouseRegisterRoute

        @Serializable
        data class Step2(val mode: RegisterModel = RegisterModel.CREATE) : HouseRegisterRoute

        @Serializable
        data class Step3(val mode: RegisterModel = RegisterModel.CREATE) : HouseRegisterRoute

        @Serializable
        data class Step4(val mode: RegisterModel = RegisterModel.CREATE) : HouseRegisterRoute
    }

    sealed interface ChatRoute : Route {
        @Serializable
        data object ChatList : ChatRoute
    }

    sealed interface MyPageRoute : Route {
        @Serializable
        data object MyPage : MyPageRoute
    }

    sealed interface NotificationRoute : Route {
        @Serializable
        data object Notification : NotificationRoute
    }
}