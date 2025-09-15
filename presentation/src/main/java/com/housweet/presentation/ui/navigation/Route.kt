package com.housweet.presentation.ui.navigation
import com.housweet.domain.model.community.Coordinate
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
            data class WelCome(val isSetProfile: Boolean, val isBelongToRoom: Boolean) : LoginRoute

            @Serializable
            data class PermissionGuide(val isSetProfile: Boolean, val isBelongToRoom: Boolean) : LoginRoute

            @Serializable
            data class TermsOfService(val isSetProfile: Boolean, val isBelongToRoom: Boolean) : LoginRoute
        }

        sealed interface AccessRoomRoute : StartPageRoute {
            @Serializable
            data class AccessRoom(val isAfterDelete: Boolean = false) : AccessRoomRoute

            @Serializable
            data object CreateRoom : AccessRoomRoute

            @Serializable
            data object SearchRoom : AccessRoomRoute
        }
    }

    sealed interface CommunityPageRoute: Route {
        @Serializable
        data class Map(val coordinate: Coordinate? = null, val userRoomStateNum: Int? = null) : CommunityPageRoute

        @Serializable
        data object SearchRegion : CommunityPageRoute

        @Serializable
        data object GuideToCreateRoom : CommunityPageRoute

        sealed interface PostRoute : CommunityPageRoute {
            @Serializable
            data class DetailPost(val postId: Int? = null, val lastRegion: String? = null, val blockedUserId: Int? = null) : PostRoute

            @Serializable
            data class Posts(val postRegions: String? = null, val updatePostId: Int? = null, val blockedUserId: Int? = null): PostRoute
        }
    }

    sealed interface HouseRegisterRoute : Route {
        @Serializable
        data class Step1(
            val mode: RegisterModel = RegisterModel.CREATE,
            val postingId: Int? = null
        ) : HouseRegisterRoute

        @Serializable
        data class Step2(
            val mode: RegisterModel = RegisterModel.CREATE,
            val postingId: Int? = null
        ) : HouseRegisterRoute

        @Serializable
        data class Step3(
            val mode: RegisterModel = RegisterModel.CREATE,
            val postingId: Int? = null
        ) : HouseRegisterRoute

        @Serializable
        data class Step4(
            val mode: RegisterModel = RegisterModel.CREATE,
            val postingId: Int? = null
        ) : HouseRegisterRoute
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